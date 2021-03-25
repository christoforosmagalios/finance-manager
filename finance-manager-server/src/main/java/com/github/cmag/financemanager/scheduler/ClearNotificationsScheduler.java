package com.github.cmag.financemanager.scheduler;

import com.github.cmag.financemanager.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler that deletes all the old and seen notifications.
 */
@Slf4j
@Component
public class ClearNotificationsScheduler {

  @Autowired
  private NotificationService notificationService;

  /**
   * Delete all the old and seen notifications.
   */
  @Scheduled(cron = "${finance.manager.cron.clear.notifications}")
  public void clearNotifications() {
    notificationService.clearNotifications();
  }
}
