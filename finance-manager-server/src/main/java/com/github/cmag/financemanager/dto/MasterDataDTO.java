package com.github.cmag.financemanager.dto;

import lombok.Data;

/**
 * A DTO to be used as the parent DTO of all other master data DTOs.
 */
@Data
public class MasterDataDTO extends BaseDTO {

  private String name;
  private String code;
  private int order;
}
