package biasiellicapodifatta.travlendar.response.responseaddactivity;

/**
 *
 * @author matteo
 */
public enum ResponseAddActivityType {
    OK("ok"),
    OK_ESTIMATED_TIME("The activity has been added to your calendar but doe to your too strict travel preferences it was not possible to calculate the estimated travel time. You may not be notified if you'll risk to be late due to the addition of another activity to the calendar."),
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
