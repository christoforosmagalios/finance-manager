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
  // MAx size 250 error message key.
  public static final String MAX_SIZE_250 = "error.size.max.250";
  // MAx size 500 error message key.
  public static final String MAX_SIZE_500 = "error.size.max.500";
}
