package com.github.cmag.financemanager.scheduler;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.service.BillService;
import com.github.cmag.financemanager.service.EmailService;
import com.github.cmag.financemanager.service.FileService;
import com.github.cmag.financemanager.service.TransactionService;
import com.github.cmag.financemanager.service.UserService;
import com.github.cmag.financemanager.util.Utils;
import com.lowagie.text.DocumentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import javax.mail.internet.MimeBodyPart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * Scheduler that generates a PDF report about last months financial overview and sends the PDF to
 * the user.
 */
@Slf4j
@Component
public class PdfReportScheduler {

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private BillService billService;

  @Autowired
  private UserService userService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private FileService fileService;

  @Value("${finance.manager.temp.path}")
  private String tempDirecotry;

  private static final String PDF_NAME = "Finance Manager - ";

  /**
   * Send email report about the bills that are going to expire soon for each user.
   */
  @Scheduled(cron = "${finance.manager.cron.pdf.report}")
  public void sendPdfReport() {
    // Find all users.
    List<UserDTO> users = userService.findAll();
    for (UserDTO user : users) {
      // Initialize the Calendar.
      Calendar cal = Calendar.getInstance();
      // Subtract one month.
      cal.add(Calendar.MONTH, -1);
      int month = cal.get(Calendar.MONTH);
      int year = cal.get(Calendar.YEAR);
      // Generate the PDF template.
      String template = generateTemplate(user, month, year);
      // Generate the PDF file name.
      String filename = PDF_NAME + Utils.getMonthName(month) + " " + year + ".pdf";
      try {
        // Create the PDF.
        File pdf = generatePdf(template, filename);
        // Send the PDF as an attachment.
        emailService
            .send(user.getEmail(), "Overview - " + Utils.getMonthName(month) + " " + year, "", pdf, javax.mail.Part.ATTACHMENT);
        // Delete the PDF.
        java.nio.file.Files.delete(pdf.toPath());
      } catch (IOException | DocumentException e) {
        log.error(e.getLocalizedMessage(), e);
      }
    }
  }

  /**
   * Generate the html template for the given user, month and year.
   *
   * @param user The user.
   * @param month The month.
   * @param year The year.
   * @return The template.
   */
  private String generateTemplate(UserDTO user, int month, int year) {
    // Initialize the template resolver.
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    // Set the template suffix.
    templateResolver.setSuffix(".html");
    // Set the template mode.
    templateResolver.setTemplateMode(TemplateMode.HTML);
    // Initialize the template engine.
    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
    // Generate the template.
    return templateEngine.process("templates/pdfReportTemplate", getContext(user, month, year));
  }

  /**
   * Construct the Context to be used in the template generation.
   *
   * @param user The user.
   * @param month The month.
   * @param year The year.
   * @return The Context.
   */
  private Context getContext(UserDTO user, int month, int year) {
    // Get the income.
    double income = transactionService.getTransactionAmount(month + 1, year, false, user.getId());
    // Get the expenses.
    double expenses = transactionService.getTransactionAmount(month + 1, year, true, user.getId());
    // Calculate the balance.
    double balance = income - expenses;
    // Get the pending amount.
    double pending = billService.getPendingAmount(user.getId());

    // Construct the Context.
    Context context = new Context();
    context.setVariable("month", Utils.getMonthName(month));
    context.setVariable("year", year);
    context.setVariable("balance", String.format(AppConstants.MONEY_FORMAT, balance));
    context.setVariable("income", String.format(AppConstants.MONEY_FORMAT, income));
    context.setVariable("expenses", String.format(AppConstants.MONEY_FORMAT, expenses));
    context.setVariable("pending", String.format(AppConstants.MONEY_FORMAT, pending));
    return context;
  }

  /**
   * Generate the PDF for the given template and the given filename.
   *
   * @param template The PDF template.
   * @param filename The PDF file name.
   * @return The PDF file.
   */
  private File generatePdf(String template, String filename) throws IOException, DocumentException {
    // Create the temp folder in case it does not already exists.
    fileService.createDirectories(tempDirecotry);
    // Initialize the output folder.
    String outputFolder = tempDirecotry + File.separator + filename;
    OutputStream outputStream = new FileOutputStream(outputFolder);
    ITextRenderer renderer = new ITextRenderer();
    renderer.setDocumentFromString(template);
    renderer.layout();
    renderer.createPDF(outputStream);
    outputStream.close();
    return new File(outputFolder);
  }
}
