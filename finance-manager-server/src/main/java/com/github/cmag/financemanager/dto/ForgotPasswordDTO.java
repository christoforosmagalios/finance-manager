package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.util.validation.EmailExists;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * Contains info about the forgot password process.
 */
@Data
@EmailExists
public class ForgotPasswordDTO {

  @NotBlank
  private String language;

  @NotBlank
  @Email(message = AppConstants.INVALID_EMAIL)
  @Pattern(regexp=".+@.+\\..+", message = AppConstants.INVALID_EMAIL)
  private String email;
}
