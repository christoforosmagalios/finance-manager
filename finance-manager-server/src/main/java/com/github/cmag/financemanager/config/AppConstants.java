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
  public static final String NOT_ALLOWED = "Not allowed!";
}
