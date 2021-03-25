package com.github.cmag.financemanager.dto;

import java.util.Map;
import lombok.Data;

/**
 * DTO representation of a Notification.
 */
@Data
public class NotificationDTO extends BaseDTO {

  private String description;
  private String icon;
  private String url;
  private boolean seen;
  private Map<String, Object> parameters;
}
