/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient.network.addactivityresponse;

/**
 *
 * @author matteo
 */
public enum ResponseAddActivityNotification {
    NO("no"),
    CANNOT_BE_ON_TIME("You'll not be able to be on time for this activity."),
    CANNOT_BE_ON_TIME_NEXT("This activity will prevent you from being on time to the successive activity."),
    OTHER("Though the calendar is consinstent, you'll not be able to be on time to some activities.");
    
    private final String message;
    ResponseAddActivityNotification(String s){
        this.message=s;
    }
    public String getMessage(){
        return this.message;
    }
}
