package com.github.cmag.financemanager.config;

/**
 * Contains application constants.
 */
public class AppConstants {

  private AppConstants() {
    // Default constructor.
  }

  // The name of the "updated on" field in the BaseEntity.
  public static final String UPDATED_ON = "updatedOn";
  // The name of the "created on" field in the BaseEntity.
  public static final String CREATED_ON = "createdOn";

  // Invalid file size error message key.
  public static final String INVALID_FILE_SIZE = "invalid.file.size";
  // Invalid file type error message key.
  public static final String INVALID_FILE_TYPE = "invalid.file.type";
  // Generic Error message key.
  public static final String GENERIC_ERROR = "generic.error";
  // Not null error message key.
  public static final String NOT_NULL = "error.not.null";
  // Max size 250 error message key.
  public static final String MAX_SIZE_250 = "error.size.max.250";
  // Max size 500 error message key.
  public static final String MAX_SIZE_500 = "error.size.max.500";
  // Min size 6 error message key.
  public static final String MIN_SIZE_6 = "error.size.min.6";
  // Min size 3 error message key.
  public static final String MIN_SIZE_3 = "error.size.min.3";
  // Passwords do not match error message key.
  public static final String PASSWORDS_DON_NOT_MATCH = "error.passwords.do.not.match";
  // Invalid email error message key.
  public static final String INVALID_EMAIL = "error.invalid.email";
  // Bill already linked with another transaction error message key.
  public static final String BILL_ALREADY_LINNKED = "error.bill.is.already.link";
  // Username already exists error message key.
  public static final String USERNAME_EXISTS = "error.username.exists";
  // Email already exists error message key.
  public static final String EMAIL_EXISTS = "error.email.exists";
  // The code of the bills transaction category.
  public static final String BILLS_TRANSACTION_CATEGORY_CODE = "TC011";
  // Token prefix.
  public static final String TOKEN_PREFIX = "Bearer ";
  // Auth header.
  public static final String AUTH_HEADER_STRING = "Authorization";
  // Auth response header.
  public static final String AUTH_RESP_HEADER_STRING = "Authorization-User";
  // Not allowed response.
  public static final String NOT_ALLOWED = "not.allowed";
  // Delete is not possible because the Bill is linked with a transaction error response.
  public static final String BILL_IS_LINKED = "error.delete.bill.is.linked";
  // Transaction type field name.
  public static final String T_TYPE = "type";
  // Transaction amount field name.
  public static final String T_AMOUNT = "amount";
  // Transaction date field name.
  public static final String T_DATE = "date";
  // Transaction billCode field name.
  public static final String T_BILL_CODE = "billCode";
  // Transaction categoryId field name.
  public static final String T_CATEGORY_ID = "categoryId";
  // The userId field name.
  public static final String USER_ID = "userId";
  // Bill issuedOn field name.
  public static final String B_ISSUED_ON = "issuedOn";
  // Bill code field name.
  public static final String B_CODE = "code";
  // Bill categoryId field name.
  public static final String B_CATEGORY_ID = "categoryId";
  // Bill paid field name.
  public static final String B_PAID = "paid";
  // Bill amount field name.
  public static final String B_AMOUNT = "amount";
  // Bill issuedOn field name.
  public static final String B_ISSUE_DATE = "issuedOn";
  // Bill dueDate field name.
  public static final String B_DUE_DATE = "dueDate";

  //Font Awesome icons
  // Bill icon
  public static final String FONT_AWESOME_BILL_ICON = "far fa-folder-open";

  // Notification Descriptions
  // Bill will expire soon description.
  public static final String NOTIFICATION_DESC_BILL_EXPIRE = "notification.bill.expire.soon";

  // Entity Endpoints
  // Bill endpoint
  public static final String BILL_ENDPOINT = "bill";

  // WebSockets
  // WebSocket endpoint.
  public static final String WS_ENDPOINT = "/notifications";
  // WebSocket user prefix.
  public static final String WS_USER_PREFIX = "/user";
  // WebSocket broker.
  public static final String WS_BROKER = "/user/queue/specific-user";


}
