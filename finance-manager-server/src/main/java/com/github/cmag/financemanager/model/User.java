package com.github.cmag.financemanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * User entity.
 */
@Entity
@Data
@Table(name = "user")
public class User extends BaseEntity {

  @Column(name = "active")
  private boolean active;

  @Column(name = "username")
  private String username;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;
}
