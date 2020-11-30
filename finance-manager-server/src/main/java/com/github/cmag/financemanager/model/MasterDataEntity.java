package com.github.cmag.financemanager.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Data;

/**
 * A master data entity abstract class to be extend by concrete Entities representing the master
 * data model.
 */
@Data
@MappedSuperclass
public class MasterDataEntity extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "order")
  private int order;
}
