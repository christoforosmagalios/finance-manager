package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.NotificationDTO;
import com.github.cmag.financemanager.mapper.NotificationMapper;
import com.github.cmag.financemanager.model.Notification;
import com.github.cmag.financemanager.repository.NotificationRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * The Notification Service.
 */
@Service
public class NotificationService extends BaseService<NotificationDTO, Notification> {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private NotificationMapper mapper;

  public List<NotificationDTO> getNotifications() {
    return mapper
        .mapToDTOs(notificationRepository.findAll(Sort.by(AppConstants.CREATED_ON).descending()));
  }

  /**
   * Mark as seen the notification with the given id.
   *
   * @param id The id of the Notification to be updated.
   */
  public void markAsSeen(String id) {
    Notification notification = notificationRepository.getOne(id);
    notification.setSeen(true);
    notificationRepository.save(notification);
  }

  /**
   * Delete all the old and seen notifications.
   */
  public void clearNotifications() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -14);
    Date date = cal.getTime();
    notificationRepository.deleteBySeenTrueAndCreatedOnBefore(date.toInstant());
  }
}
