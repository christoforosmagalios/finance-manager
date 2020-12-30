package com.github.cmag.financemanager.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO representation of transactions grouped by their category.
 */
@Data
@AllArgsConstructor
public class GroupedTransactionDTO {

  private LocalDate from;
  private LocalDate to;
  List<GroupedTransactionInfoDTO> transactions;

}
