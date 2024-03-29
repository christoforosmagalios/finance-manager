package com.github.cmag.financemanager.scheduler;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.mapper.NotificationMapper;
import com.github.cmag.financemanager.mapper.UserMapper;
import com.github.cmag.financemanager.model.Notification;
import com.github.cmag.financemanager.model.NotificationDescriptionParameter;
import com.github.cmag.financemanager.service.BillService;
import com.github.cmag.financemanager.service.EmailService;
import com.github.cmag.financemanager.service.NotificationService;
import com.github.cmag.financemanager.service.UserService;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler that sends a mail to every user with the bills that expire soon.
 */
@Component
public class BillsExpireSoonScheduler {

  @Autowired
  private EmailService emailService;

  @Autowired
  private BillService billService;

  @Autowired
  private UserService userService;

  @Autowired
  private NotificationService notificationService;

  @Autowired
  private SimpMessagingTemplate messageSender;

  @Autowired
  private NotificationMapper notificationMapper;

  @Autowired
  private UserMapper userMapper;

  private static final String SUBJECT = "Some Bills will expire soon";
  private static final String PADDING = "padding:7px;";
  private static final String TH_STYLE = "border-bottom: 1px solid #cccccc;color:#4e73df;";
  private static final String TH = "th";
  private static final String TD = "th";
  private static final String STYLE = "style";
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  /**
   * Send email report about the bills that are going to expire soon for each user.
   */
  @Scheduled(cron = "${finance.manager.cron.bills.expire.soon}")
  public void billsExpireSoon() {
    // Find all users.
    List<UserDTO> users = userService.findAll();
    for (UserDTO user : users) {
      // Find the bills that expire soon or already have expired.
      List<BillDTO> bills = billService.findBillsThatExpireSoonByUserId(user.getId());
      if (!bills.isEmpty()) {
        sendExpiringReport(user, bills);
        addNotifications(user, bills);
      }
    }
  }

  /**
   * Construct and add a notification about the Bills that will expire soon.
   *
   * @param bills The bills that will expire soon.
   */
  private void addNotifications(UserDTO user, List<BillDTO> bills) {
    // Iterate through each Bill.
    for (BillDTO bill : bills) {
      // Construct a new notification about the Bill.
      Notification notification = new Notification();
      notification.setDescription(AppConstants.NOTIFICATION_DESC_BILL_EXPIRE);
      notification.setIcon(AppConstants.FONT_AWESOME_BILL_ICON);
      notification.setUrl("/" + AppConstants.BILL_ENDPOINT + "/" + bill.getId());
      notification.setUser(userMapper.map(user));
      // Add description parameters.
      NotificationDescriptionParameter code = new NotificationDescriptionParameter("code",
          bill.getCode());
      NotificationDescriptionParameter date = new NotificationDescriptionParameter("date",
          bill.getDueDate().format(formatter));
      notification.setParams(List.of(code, date));
      // Save the notification.
      notificationService.save(notification);
      // Send a notification to the UI.
      messageSender
          .convertAndSendToUser(user.getUsername(), AppConstants.WS_BROKER, notificationMapper.map(notification));
    }
  }

  /**
   * Generate the email body and send the email.
   *
   * @param user The user.
   * @param bills The list of bills.
   */
  private void sendExpiringReport(UserDTO user, List<BillDTO> bills) {
    // The email body template.
    String body = "<html><body style=\"font-family: sans-serif;\"><p>Hi {0} {1},</p><p>These Bills "
        + "will expire soon:</p><div>{2}</div><p>Finance Manager.</p></body></html>";
    // The bills table template.
    String table = "<table style=\"border-collapse: collapse;margin: 25px 0px 25px 10px;\"><tr>"
        + "<" + TH + " " + STYLE +"=\"" + PADDING + TH_STYLE + "\">CODE</" + TH + ">"
        + "<" + TH + " " + STYLE +"=\"" + PADDING + TH_STYLE + "\">DESCRIPTION</" + TH + ">"
        + "<" + TH + " " + STYLE +"=\"" + PADDING + TH_STYLE + "\">ISSUED ON</" + TH + ">"
        + "<" + TH + " " + STYLE +"=\"" + PADDING + TH_STYLE + "\">DUE DATE</" + TH + ">"
        + "<" + TH + " " + STYLE +"=\"" + PADDING + TH_STYLE + "\">AMOUNT</" + TH + ">"
        + "</tr>{0}</table>";
    // Generate the rows and add them to the table.
    table = MessageFormat.format(table, generateTableRows(bills));
    // Fill in the body template.
    body = MessageFormat.format(body, user.getFirstName(), user.getLastName(), table);
    // Send the email.
    emailService.send(user.getEmail(), SUBJECT, body, null, null);
  }

  /**
   * Generate html table rows for the given bills.
   *
   * @param bills A list of bills.
   * @return The html rows.
   */
  private String generateTableRows(List<BillDTO> bills) {
    // Initialize a string builder.
    StringBuilder rows = new StringBuilder();
    for (BillDTO bill : bills) {
      // Initialize the expired bill style.
      String expired = "";
      // If the bill has expired, add style to the cell.
      if (LocalDate.now().isAfter(bill.getDueDate())) {
        expired = "color: #e74a3b;";
      }

      rows.append(
          "<tr>"
              + "<" + TD + " " + STYLE +"=\"" + PADDING + "\">" + bill.getCode() + "</" + TD + ">"
              + "<" + TD + " " + STYLE +"=\"" + PADDING + "\">" + bill.getDescription() + "</" + TD + ">"
              + "<" + TD + " " + STYLE +"=\"" + PADDING + "\">" + bill.getIssuedOn().format(formatter) + "</" + TD + ">"
              + "<" + TD + " " + STYLE +"=\"" + PADDING + expired + "\">" + bill.getDueDate().format(formatter)
              + "</" + TD + ">"
              + "<" + TD + " " + STYLE +"=\"" + PADDING + "text-align:right;\">" + bill.getAmount() + " €</" + TD + ">"
              + "</tr>");
    }
    return rows.toString();
  }


}
