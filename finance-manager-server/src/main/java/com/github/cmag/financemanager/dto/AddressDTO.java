package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.config.AppConstants;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * DTO representation of a Address.
 */
@Data
public class AddressDTO extends BaseDTO {

  @NotEmpty(message = AppConstants.NOT_NULL)
  @Size(max = 250, message = AppConstants.MAX_SIZE_250)
  private String description;
}
