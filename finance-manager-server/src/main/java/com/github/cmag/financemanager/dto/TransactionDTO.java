package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.master.data.TransactionCategoryDTO;
import com.github.cmag.financemanager.util.validation.NotAlreadyLinked;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * DTO representation of a Transaction.
 */
@Data
@NotAlreadyLinked
public class TransactionDTO extends BaseDTO {

  @NotNull(message = AppConstants.NOT_NULL)
  private TransactionCategoryDTO transactionCategory;

  @NotNull(message = AppConstants.NOT_NULL)
  private boolean type;

  @Size(max = 500, message = AppConstants.MAX_SIZE_500)
  private String notes;

  @NotEmpty(message = AppConstants.NOT_NULL)
  @Size(max = 250, message = AppConstants.MAX_SIZE_250)
  private String description;

  @NotNull(message = AppConstants.NOT_NULL)
  private double amount;

  @NotNull(message = AppConstants.NOT_NULL)
  private Date date;
  private BillDTO bill;
}
