package travlendarplus.response.responseretrievenotifications;

import travlendarplus.notification.Notification;

import java.util.ArrayList;

/**
 * Modified by mattiadifatta on 24/11/2017.
 */
public class ResponseRetrieveNotifications {
    private ArrayList<Notification> notifications;
    private ResponseRetrieveNotificationsType type;

    public ResponseRetrieveNotifications(ArrayList<Notification> notif, ResponseRetrieveNotificationsType type){
        this.notifications = notif;
        this.type = type;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public ResponseRetrieveNotificationsType getType() {
        return type;
    }
}
