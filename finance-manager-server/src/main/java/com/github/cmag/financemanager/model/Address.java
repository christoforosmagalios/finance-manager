package com.github.cmag.financemanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * Address entity.
 */
@Entity
@Data
@Table(name = "address")
public class Address extends BaseEntity {

  @Column(name = "description")
  private String description;
}
