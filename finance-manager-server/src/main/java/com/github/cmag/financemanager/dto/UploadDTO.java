package com.github.cmag.financemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO representation of a image file.
 */
@Data
@AllArgsConstructor
public class UploadDTO {

  private String base64;
  private String imgPath;
  private String imgType;
}
