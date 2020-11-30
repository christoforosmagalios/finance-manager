package com.github.cmag.financemanager.dto;

import java.util.Date;
import lombok.Data;

/**
 * DTO representation of a Transaction.
 */
@Data
public class TransactionDTO extends BaseDTO {

  private CategoryDTO category;
  private boolean type;
  private String notes;
  private String description;
  private double amount;
  private Date date;
}
