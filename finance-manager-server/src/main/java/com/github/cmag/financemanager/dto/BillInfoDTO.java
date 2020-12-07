package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.dto.master.data.BillCategoryDTO;
import java.util.Date;
import lombok.Data;

/**
 * DTO representation that contains information about a Bill.
 */
@Data
public class BillInfoDTO extends BaseDTO {

  private BillCategoryDTO billCategory;
  private Date issuedOn;
  private Date dueDate;
  private String description;
  private double amount;
  private boolean paid;
}
