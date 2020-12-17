package com.github.cmag.financemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO representation of transactions info grouped by their category.
 */
@Data
@AllArgsConstructor
public class GroupedTransactionInfoDTO {
  private String category;
  private String color;
  private double amount;
}
