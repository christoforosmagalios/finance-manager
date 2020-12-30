package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.dto.master.data.TransactionCategoryDTO;
import java.time.LocalDate;
import lombok.Data;

/**
 * DTO representation of a Transaction Filter.
 */
@Data
public class TransactionFilterDTO {

  private Boolean type;
  private TransactionCategoryDTO transactionCategory;
  private String billCode;
  private LocalDate dateFrom;
  private LocalDate dateTo;
  private double amountFrom;
  private double amountTo;
}
