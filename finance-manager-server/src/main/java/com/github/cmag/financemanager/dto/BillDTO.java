package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.config.AppConstants;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * DTO representation of a Bill.
 */
@Data
public class BillDTO extends BillInfoDTO {

  private Date paidOn;

  @NotNull(message = AppConstants.NOT_NULL)
  private Date consumptionFrom;

  @NotNull(message = AppConstants.NOT_NULL)
  private Date consumptionTo;

  @Size(max = 250, message = AppConstants.MAX_SIZE_500)
  private String notes;
  private double unpaidAmount;

  @NotNull(message = AppConstants.NOT_NULL)
  private boolean actualBill;
  private String imgPath;
  private String imgType;
  private String base64;
}
