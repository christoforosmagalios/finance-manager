package com.github.cmag.financemanager.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupedTransactionDTO {

  private Date from;
  private Date to;
  List<GroupedTransactionInfoDTO> transactions;

}
