package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.util.exception.FinanceManagerException;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import javax.mail.internet.MimeBodyPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
  private TranslationService translationService;

  /**
   * Find the User with the given email and send a new password request email in the given
   * language.
   *
   * @param email The email.
   * @param language The language.
   */
  public void forgotPassword(String email, String language) {
    // Find the user with the given email.
    UserDTO user = userService.findByEmail(email);
    // Init the full name of the user.
    String name = user.getFirstName() + " " + user.getLastName();
    // Generate the reset password email body.
    String body = generateTemplate(name, language);
    // Get the email subject.
    String subject = translationService.translate("password.reset.mail.subject", language);
    // Send the email.
    boolean sent = emailService
        .send(email, subject, body, getFinanceManagerLogo(), MimeBodyPart.INLINE);
    // In case the email could not be sent, throw an exception.
    if (!sent) {
      throw new FinanceManagerException(AppConstants.GENERIC_ERROR, HttpStatus.BAD_REQUEST);
    }
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
   * @param language The language of the email.
   * @return The Email body.
   */
  private String generateTemplate(String name, String language) {
    // Initialize the template resolver.
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    // Set the template suffix.
    templateResolver.setSuffix(".html");
    // Set the template mode.
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCharacterEncoding("UTF-8");
    // Initialize the template engine.
    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
    Context context = new Context();
    context.setVariable("name", name);
    // Generate the template.
    return templateEngine.process("templates/forgotPassword/template_" + language, context);
  }
}
