package com.github.cmag.financemanager.dto;

import lombok.Data;

/**
 * DTO representation of a User.
 */
@Data
public class UserDTO extends BaseDTO {

  private boolean active;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
}
