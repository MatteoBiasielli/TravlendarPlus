package biasiellicapodifatta.travlendar.response.responseaddactivity;

import biasiellicapodifatta.travlendar.data.user.User;

/**
 *
 * @author matteo
 */
public class ResponseAddActivity {
    private final User u;
    private final ResponseAddActivityType type;
    private final ResponseAddActivityNotification notification;
    public ResponseAddActivity(User u, ResponseAddActivityType raat, ResponseAddActivityNotification raan){
        this.u=u;
        this.type=raat;
        this.notification=raan;
    }
    public User getUser(){
        return this.u;
    }
    public ResponseAddActivityType getType(){
        return this.type;
    }
    public ResponseAddActivityNotification getNotification(){
        return this.notification;
    }
    
    
}
