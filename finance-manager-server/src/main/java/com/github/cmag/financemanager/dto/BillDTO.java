package com.github.cmag.financemanager.dto;

import com.github.cmag.financemanager.dto.master.data.BillCategoryDTO;
import java.util.Date;
import lombok.Data;

/**
 * DTO representation of a Bill.
 */
@Data
public class BillDTO extends BaseDTO {

  private BillCategoryDTO billCategory;
  private Date issuedOn;
  private Date dueDate;
  private Date paidOn;
  private Date consumptionFrom;
  private Date consumptionTo;
  private String description;
  private String notes;
  private double amount;
  private double unpaidAmount;
  private boolean paid;
  private boolean actualBill;
  private String imgPath;
  private String imgType;
  private String base64;
}
