package com.github.cmag.financemanager.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * Transaction entity.
 */
@Entity
@Data
@Table(name = "transaction")
public class Transaction extends BaseEntity {

  @Column(name = "type")
  private boolean type;

  @ManyToOne
  @JoinColumn(name = "fk_category_id", nullable = false)
  private Category category;

  @OneToOne
  @JoinColumn(name = "fk_bill_id")
  private Bill bill;

  @Column(name = "date")
  private Date date;

  @Column(name = "description")
  private String description;

  @Column(name = "notes")
  private String notes;

  @Column(name = "amount")
  private double amount;
}
