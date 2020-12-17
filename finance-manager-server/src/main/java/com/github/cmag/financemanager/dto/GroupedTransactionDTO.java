package com.github.cmag.financemanager.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO representation of transactions grouped by their category.
 */
@Data
@AllArgsConstructor
public class GroupedTransactionDTO {

  private Date from;
  private Date to;
  List<GroupedTransactionInfoDTO> transactions;

}
