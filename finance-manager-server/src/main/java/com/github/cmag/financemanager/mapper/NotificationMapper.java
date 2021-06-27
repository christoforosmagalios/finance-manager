package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.NotificationDTO;
import com.github.cmag.financemanager.model.Notification;
import com.github.cmag.financemanager.model.NotificationDescriptionParameter;
import org.json.JSONObject;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NotificationMapper extends BaseMapper<NotificationDTO, Notification> {

  /**
   * Get the notification description parameters and convert them into a map.
   *
   * @param notification The Notification source.
   * @param notificationDTO The NotificationDTO target.
   */
  @AfterMapping
  default void mapNotificationParams(Notification notification,
      @MappingTarget NotificationDTO notificationDTO) {

    JSONObject obj = new JSONObject();
    for (NotificationDescriptionParameter param : notification.getParams()) {
      obj.put(param.getName(), param.getValue());
    }
    notificationDTO.setParameters(obj.toMap());
  }
}
