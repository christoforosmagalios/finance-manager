
import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import * as SockJS from 'sockjs-client';
import { NotificationDTO } from "src/app/dto/notification-dto";
import { Constants } from "../constants/constants";

declare var Stomp;

@Injectable({providedIn: 'root'})
export class MessageService {
    
    socket: SockJS;
    stompClient;
    notification = new BehaviorSubject<NotificationDTO>(null);

    constructor() {}

    /**
     * Initialize a SockJS instance and connect to the WebSocket.
     */
    connect() {
        // Create a random session Id.
        let sessionId = Math.random().toString(36).substring(7);
        // Initialize the Sockjs instance.
        this.socket = new SockJS(Constants.API + Constants.WS.ENDPOINT, [], {
            sessionId: () => {
                return sessionId
             }
        }); 

        // Initialize the stomp client.
        this.stompClient = Stomp.over(this.socket);
        // Disable the debug mode.
        this.stompClient.debug = false;
        this.stompClient.connect({}, (frame) => {
            this.stompClient.subscribe(Constants.WS.USER_BROKER + '-user' + sessionId, (message) => {
                this.notification.next(JSON.parse(message.body));
            });
        });
    }

    /**
     * Close the Stomp client and the Websocket.
     */
    disconnect() {
        if (this.stompClient !== null && this.stompClient !== undefined) { 
            this.stompClient.disconnect(); 
        }
        if (this.socket !== null && this.socket !== undefined) { 
            this.socket.close();
        }
    }
}
