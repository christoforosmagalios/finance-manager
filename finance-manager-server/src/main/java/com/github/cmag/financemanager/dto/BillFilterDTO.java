package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.dto.master.data.BillCategoryDTO;
import java.time.LocalDate;
import lombok.Data;

/**
 * DTO representation of a Bill Filter.
 */
@Data
public class BillFilterDTO {

  private Boolean paid;
  private BillCategoryDTO billCategory;
  private String code;
  private LocalDate issueDateFrom;
  private LocalDate issueDateTo;
  private LocalDate dueDateFrom;
  private LocalDate dueDateTo;
  private double amountFrom;
  private double amountTo;
}
