package travlendardesktopclient.network.updateactivityresponse;

import travlendardesktopclient.data.user.User;
import travlendardesktopclient.network.addactivityresponse.ResponseAddActivityNotification;


/**
 * Modified by mattiadifatta on 21/11/2017.
 */
public class ResponseUpdateActivity {
    private final User u;
    private final ResponseUpdateActivityType type;
    private final ResponseAddActivityNotification notification;

    public ResponseUpdateActivity(User u, ResponseUpdateActivityType type, ResponseAddActivityNotification notification){
        this.u = u;
        this.type = type;
        this.notification = notification;
    }

    public User getU() {
        return u;
    }

    public ResponseAddActivityNotification getNotification() {
        return notification;
    }

    public ResponseUpdateActivityType getType() {
        return type;
    }
}
