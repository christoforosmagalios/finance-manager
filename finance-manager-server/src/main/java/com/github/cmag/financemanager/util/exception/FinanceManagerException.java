package com.github.cmag.financemanager.util.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Custom Exception.
 */
@Data
@AllArgsConstructor
public class FinanceManagerException extends RuntimeException {

  private final String message;
  private final HttpStatus status;

  public FinanceManagerException(String message) {
    this.message = message;
    this.status = HttpStatus.BAD_REQUEST;
  }
}
