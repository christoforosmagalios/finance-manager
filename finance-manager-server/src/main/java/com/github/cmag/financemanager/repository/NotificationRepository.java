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

  List<Notification> deleteBySeenTrueAndCreatedOnBefore(Instant date);
}
