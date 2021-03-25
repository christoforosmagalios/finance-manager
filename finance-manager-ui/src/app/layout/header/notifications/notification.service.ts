import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { NotificationDTO } from 'src/app/dto/notification-dto';
import { Constants } from 'src/app/shared/constants/constants';

@Injectable({providedIn: 'root'})
export class NotificationService {
    endpoint = Constants.ENTITY.NOTIFICATION;

    constructor(private http: HttpClient) {}

    /**
     * Fetch the latest notifications.
     */
    getLatestNotifications() {
        return this.http.get<Array<NotificationDTO>>(Constants.API + '/' + this.endpoint + '/latest');
    }

    /**
     * Mark the Notification with the given id as seen.
     * 
     * @param id The notification id.
     */
    markAsSeen(id: string) {
        return this.http.post(Constants.API + '/' + this.endpoint + '/markAsSeen', id);
    }
}