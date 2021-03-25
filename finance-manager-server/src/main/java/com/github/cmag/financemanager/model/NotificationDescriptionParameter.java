package com.github.cmag.financemanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Notification Description Parameter entity.
 */
@Entity
@Data
@Table(name = "notification_description_parameter")
@NoArgsConstructor
public class NotificationDescriptionParameter extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "value")
  private String value;

  public NotificationDescriptionParameter(String name, String value) {
    this.name = name;
    this.value = value;
  }
}
