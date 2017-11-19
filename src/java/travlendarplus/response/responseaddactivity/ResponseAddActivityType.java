/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responseaddactivity;

/**
 *
 * @author matteo
 */
public enum ResponseAddActivityType {
    OK("ok"),
    ADD_ACTIVITY_LOGIN_ERROR("The login data are invalid."),
    ADD_ACTIVITY_OVERLAPPING("The activity overlaps with the others"),
    ADD_ACTIVITY_PAST("Cannot add Activities in the past."),
    ADD_ACTIVITY_WRONG_INPUT("The given input data are invalid."),
    ADD_ACTIVITY_CONNECTION_ERROR("We had a connection problem, please retry soon.");
    private final String message;
    
    ResponseAddActivityType(String s){
        this.message=s;
    }
    public String getMessage(){
        return message;
    }
}
