package com.github.cmag.financemanager.model;

import com.github.cmag.financemanager.model.master.data.BillCategory;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * Bill entity.
 */
@Entity
@Data
@Table(name = "bill")
public class Bill extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "fk_bill_category_id", nullable = false)
  private BillCategory billCategory;

  @Column(name = "issued_on")
  private Date issuedOn;

  @Column(name = "due_date")
  private Date dueDate;

  @Column(name = "paid_on")
  private Date paidOn;

  @Column(name = "consumption_from")
  private Date consumptionFrom;

  @Column(name = "consumption_to")
  private Date consumptionTo;

  @Column(name = "description")
  private String description;

  @Column(name = "notes")
  private String notes;

  @Column(name = "amount")
  private double amount;

  @Column(name = "unpaid_amount")
  private double unpaidAmount;

  @Column(name = "paid")
  private boolean paid;

  @Column(name = "actual_bill")
  private boolean actualBill;
}
