package com.github.cmag.financemanager.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 * Notification entity.
 */
@Entity
@Data
@Table(name = "notification")
public class Notification extends BaseEntity {

  @Column(name = "description")
  private String description;

  @Column(name = "icon")
  private String icon;

  @Column(name = "url")
  private String url;

  @Column(name = "seen")
  private boolean seen;

  @ManyToOne
  @JoinColumn(name = "fk_user_id")
  private User user;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "fk_notification_id", referencedColumnName = "id")
  private List<NotificationDescriptionParameter> params;
}
