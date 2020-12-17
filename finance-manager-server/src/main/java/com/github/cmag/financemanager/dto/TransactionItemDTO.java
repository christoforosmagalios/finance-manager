package com.github.cmag.financemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Transaction expense/income info including a label. To be used in Line charts.
 */
@Data
@AllArgsConstructor
public class TransactionItemDTO {

  private String label;
  private double income;
  private double expense;
}
