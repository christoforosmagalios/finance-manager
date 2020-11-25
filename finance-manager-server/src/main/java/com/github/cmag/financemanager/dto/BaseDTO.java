package com.github.cmag.financemanager.dto;

import java.time.Instant;
import lombok.Data;

/**
 * A DTO to be used as the parent DTO of all other DTOs.
 */
@Data
public class BaseDTO {

  private String id;
  private Instant createdOn;
  private Instant updatedOn;

}
