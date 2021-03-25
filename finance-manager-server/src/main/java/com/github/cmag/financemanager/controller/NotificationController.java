package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.NotificationDTO;
import com.github.cmag.financemanager.model.Notification;
import com.github.cmag.financemanager.service.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Notification Rest controller.
 */
@RestController
@RequestMapping("/notification")
public class NotificationController extends BaseController<NotificationDTO, Notification> {

  @Autowired
  private NotificationService notificationService;

  /**
   * Fetch the latest notifications.
   *
   * @return A list of notifications.
   */
  @GetMapping("/latest")
  public List<NotificationDTO> getall() {
    return notificationService.getNotifications();
  }

  /**
   * Mark the notification with the given id as seen.
   *
   * @param id The notification id.
   */
  @PostMapping("/markAsSeen")
  public void markAsSeen(@RequestBody String id) {
    notificationService.markAsSeen(id);
  }

}
