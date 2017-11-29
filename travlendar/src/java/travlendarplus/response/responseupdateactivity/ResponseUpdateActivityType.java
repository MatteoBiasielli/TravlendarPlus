package travlendarplus.response.responseupdateactivity;

/**
 * Modified by mattiadifatta on 21/11/2017.
 */
public enum ResponseUpdateActivityType {
    OK("ok"),
    OK_ESTIMATED_TIME("The activity has been modified in your calendar but due to your too strict travel preferences it was not possible to calculate the estimated travel time. You may not be notified if you'll risk to be late due to the addition of another activity to the calendar"),
    UPDATE_ACTIVITY_LOGIN_ERROR("The login data are invalid."),
    UPDATE_ACTIVITY_OVERLAPPING_ERROR("The new activity overlaps with the others"),
    UPDATE_ACTIVITY_PAST_INSERTION("Ops!Cannot add activities in the past"),
    UPDATE_ACTIVITY_WRONG_INPUT("The given input data are invalid."),
    UPDATE_ACTIVITY_CONN_ERROR("We had a connection problem, our servers are lazy ;), please retry soon.");

    private final String message;

    ResponseUpdateActivityType(String s){
        this.message = s;
    }

    public String getMessage(){
        return this.message;
    }
}
