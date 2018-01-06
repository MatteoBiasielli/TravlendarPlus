/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responseaddactivity;

import travlendarplus.user.User;

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
