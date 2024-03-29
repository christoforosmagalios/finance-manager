package com.github.cmag.financemanager.model;

import com.github.cmag.financemanager.model.master.data.TransactionCategory;
import java.time.LocalDate;
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
  @JoinColumn(name = "fk_transaction_category_id", nullable = false)
  private TransactionCategory transactionCategory;

  @OneToOne
  @JoinColumn(name = "fk_bill_id")
  private Bill bill;

  @Column(name = "date")
  private LocalDate date;

  @Column(name = "description")
  private String description;

  @Column(name = "notes")
  private String notes;

  @Column(name = "amount")
  private double amount;

  @ManyToOne
  @JoinColumn(name = "fk_user_id")
  private User user;
}
