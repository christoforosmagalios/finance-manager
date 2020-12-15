package com.github.cmag.financemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupedTransactionInfoDTO {
  private String category;
  private double amount;
}
