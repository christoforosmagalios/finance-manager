package com.github.cmag.financemanager.model.master.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * TransactionCategory entity.
 */
@Entity
@Data
@Table(name = "transaction_category")
public class TransactionCategory extends MasterDataEntity {

  @Column(name = "color")
  private String color;
}
