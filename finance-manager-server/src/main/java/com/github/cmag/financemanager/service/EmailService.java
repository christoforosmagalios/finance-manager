package com.github.cmag.financemanager.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

  @Value("${mail.smtp.auth}")
  private String auth;
  @Value("${mail.smtp.starttls.enable}")
  private String starttls;
  @Value("${mail.smtp.host}")
  private String host;
  @Value("${mail.smtp.port}")
  private String port;
  @Value("${mail.smtp.ssl.trust}")
  private String ssl;
  @Value("${mail.smtp.user}")
  private String username;
  @Value("${mail.smtp.password}")
  private String password;
  @Value("${mail.sender.name}")
  private String sender;

  private static final String PROTOCOL = "smtp";

  /**
   * Send an email to the given recipient with the given body and subject.
   *
   * @param recipient The email recipient.
   * @param subject The email subject.
   * @param body The email body.
   * @return true if the mail has been sent, false otherwise.
   */
  public boolean send(String recipient, String subject, String body, File file) {
    try {
      Session session = getSession();
      Message message = getMessage(session, subject, recipient);

      Multipart multipart = new MimeMultipart();
      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setContent(body, "text/html;charSet=UTF-8");

      MimeBodyPart attachmentPart = new MimeBodyPart();
      if (!Objects.isNull(file)) {
        attachmentPart.attachFile(file);
      }

      multipart.addBodyPart(mimeBodyPart);
      multipart.addBodyPart(attachmentPart);

      message.setContent(multipart);

      Transport transport = session.getTransport(PROTOCOL);
      transport.connect(host, username, password);
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
    } catch (IOException | MessagingException e) {
      log.error(e.getLocalizedMessage(), e);
      return false;
    }
    return true;
  }

  /**
   * Get the Message to be send.
   *
   * @param session The Session.
   * @param subject The subject.
   * @param recipient The recipient.
   * @return The Message
   */
  private Message getMessage(Session session, String subject, String recipient)
      throws MessagingException, UnsupportedEncodingException {
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(username, sender));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
    message.setSubject(subject);
    return message;
  }

  /**
   * Get the Session with all the smtp properties.
   *
   * @return The Session.
   */
  private Session getSession() {
    Properties prop = new Properties();
    prop.put("mail.smtp.auth", auth);
    prop.put("mail.smtp.port", port);
    prop.put("mail.smtp.ssl.trust", ssl);
    prop.put("mail.smtp.starttls.enable", starttls);
    prop.put("mail.smtp.user", username);
    prop.put("mail.smtp.password", password);
    return Session.getDefaultInstance(prop);
  }
}
