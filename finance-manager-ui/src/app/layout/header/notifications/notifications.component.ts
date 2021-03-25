import { Component, OnInit } from '@angular/core';
import { NotificationDTO } from 'src/app/dto/notification-dto';
import { NotificationService } from './notification.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  // The notifications list.
  notifications: NotificationDTO[] = [];
  // The number of new notifications.
  newNotifications = 0;

  constructor(private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.notificationService.getLatestNotifications().subscribe(notifications => {
      this.notifications = notifications;
      this.calculateNewNotifications();
    });
  }

  /**
   * To be triggered when the user's mouse leaves the container of an
   * unseen notification. Mark this notification as seen.
   * 
   * @param notification The notification the user hovered.
   */
  markAsSeen(notification: NotificationDTO) {
    if (!notification.seen) {
      this.notificationService.markAsSeen(notification.id).subscribe();
      notification.seen = true;
      this.calculateNewNotifications();
    }
  }

  /**
   * Update the new notifications count.
   */
  private calculateNewNotifications() {
    this.newNotifications = this.notifications.filter((obj) => obj.seen === false).length;
  }

}
