package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.dto.UserPasswordDetailsDTO;
import com.github.cmag.financemanager.mapper.UserMapper;
import com.github.cmag.financemanager.model.User;
import com.github.cmag.financemanager.repository.UserRepository;
import com.github.cmag.financemanager.util.AES;
import com.github.cmag.financemanager.util.exception.FinanceManagerException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * The Password Reset Service.
 */
@Service
@Transactional
public class PasswordResetService {

  @Autowired
  private EmailService emailService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private TranslationService translationService;

  @Autowired
  private AES aes;

  @Value("${server.servlet.context-path}")
  private String contextPath;

  @Autowired
  private BCryptPasswordEncoder encoder;

  /**
   * Find the User with the given email and send a new password request email in the given
   * language.
   *
   * @param email The email.
   * @param language The language.
   * @param request The HttpServletRequest.
   */
  public void forgotPassword(String email, String language, HttpServletRequest request) {
    // Find the user with the given email.
    UserDTO user = userService.findByEmail(email);
    // Init the full name of the user.
    String name = user.getFirstName() + " " + user.getLastName();
    // Get the link of the email's button.
    String link = getButtonUrl(user, request);
    // Generate the reset password email body.
    String body = generateTemplate(name, link, language);
    // Get the email subject.
    String subject = translationService.translate("password.reset.mail.subject", language);
    // Send the email.
    boolean sent = emailService
        .send(email, subject, body, getFinanceManagerLogo(), javax.mail.Part.INLINE);
    // In case the email could not be sent, throw an exception.
    if (!sent) {
      throw new FinanceManagerException(AppConstants.GENERIC_ERROR, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Extract the app URL from the given HttpServletRequest.
   *
   * @param request The HttpServletRequest.
   * @return The app URL.
   */
  private String getAppUrl(HttpServletRequest request) {
    String siteURL = request.getRequestURL().toString();
    return siteURL.replace(contextPath + request.getServletPath(), "");
  }

  /**
   * Construct the button's link in the email.
   *
   * @param user The user.
   * @param request The HttpServletRequest.
   * @return The Link.
   */
  private String getButtonUrl(UserDTO user, HttpServletRequest request) {
    try {
      // Generate the encrypted string that will contain the username and the expiration date of the
      // reset password request.
      String encryptedInfo = generateEncryptedInfo(user);
      // Construct the URI.
      URI uri = new URI(getAppUrl(request) + "/resetPassword/" + URLEncoder
          .encode(encryptedInfo, StandardCharsets.UTF_8.toString()));
      return uri.toURL().toString();
    } catch (MalformedURLException | UnsupportedEncodingException | URISyntaxException e){
      throw new FinanceManagerException(AppConstants.GENERIC_ERROR, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Generate the encrypted reset password string which contains the username and the expiration
   * Date.
   *
   * @param user The User.
   * @return The encrypted string.
   */
  private String generateEncryptedInfo(UserDTO user) {
    LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(60, ChronoUnit.MINUTES));
    Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    return aes.encrypt(user.getUsername() + "|" + date.getTime());
  }

  /**
   * Retrieve the Finance Manager Logo.
   *
   * @return The File which contains the Logo.
   */
  private File getFinanceManagerLogo() {
    try {
      URL resource = getClass().getClassLoader().getResource("templates/images/logo.png");
      return new File(resource.toURI());
    } catch (URISyntaxException e) {
      throw new FinanceManagerException(AppConstants.GENERIC_ERROR, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Generate the password reset email body with the given parameters.
   *
   * @param name The full name of the user.
   * @param link The link of the button.
   * @param language The language of the email.
   * @return The Email body.
   */
  private String generateTemplate(String name, String link, String language) {
    // Initialize the template resolver.
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    // Set the template suffix.
    templateResolver.setSuffix(".html");
    // Set the template mode.
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    // Initialize the template engine.
    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
    Context context = new Context();
    context.setVariable("name", name);
    context.setVariable("link", link);
    // Generate the template.
    return templateEngine.process("templates/forgotPassword/template_" + language, context);
  }

  /**
   * Validate the given encrypted reset password id and fetch the user.
   *
   * @param id The encrypted id to be validated.
   * @return The user if the id is valid, null otherwise.
   */
  public UserDTO validateResetPasswordId(String id) {
    // Try to decrypt the given encrypted string.
    String decryptedStr = aes.decrypt(id);
    if (!StringUtils.isBlank(decryptedStr)) {
      String[] params = decryptedStr.split("\\|");
      if (params.length == 2) {
        String username = params[0];
        // If the expiration date is valid, proceed.
        if (Instant.now().toEpochMilli() < Long.valueOf(params[1])) {
          UserDTO user = userService.findByUsername(username);
          if (!Objects.isNull(user)) {
            return user;
          }
        }
      }
    }
    return null;
  }

  /**
   * Change the user's password.
   *
   * @param userPasswordDetailsDTO Contains the change password information.
   */
  public void changePassword(UserPasswordDetailsDTO userPasswordDetailsDTO) {
    // Validate the reset password id and fetch the user.
    UserDTO userDTO = validateResetPasswordId(userPasswordDetailsDTO.getResetPasswordId());
    if (Objects.isNull(userDTO)) {
      throw new FinanceManagerException(AppConstants.GENERIC_ERROR);
    }
    User user = userMapper.map(userDTO);
    // Encrypt and set the new password.
    user.setPassword(encoder.encode(userPasswordDetailsDTO.getPassword()));
    // Save the user.
    userRepository.save(user);
  }
}
