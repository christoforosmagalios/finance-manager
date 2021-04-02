package com.github.cmag.financemanager.repository;

import com.github.cmag.financemanager.model.Notification;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * The Notification Repository.
 */
@Repository
public interface NotificationRepository extends BaseRepository<Notification> {

  /**
   * Delete the seen notifications that are older than the given date.
   *
   * @param date The Date.
   */
  void deleteBySeenTrueAndCreatedOnBefore(Instant date);

  /**
   * Find the notifications that belong to the user with the given id.
   *
   * @param userId The User id.
   * @return A list of Notifications.
   */
  List<Notification> findByUserIdOrderByCreatedOnDesc(String userId);
}
