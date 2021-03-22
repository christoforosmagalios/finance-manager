package com.github.cmag.financemanager.dto;

import lombok.Data;

/**
 * DTO representation of a Search result.
 */
@Data
public class SearchResultDTO {

  private String id;
  private boolean bill;
  private float score;
  private String description;

}
