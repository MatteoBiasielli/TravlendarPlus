package biasiellicapodifatta.travlendar.response.responsetravel;

/**
 *
 * @author matteo
 */
public enum ResponseTravelType {
    OK("ok"),
    TRAVEL_LOGIN_ERROR("The login data are invalid."),
    TRAVEL_WRONG_INPUT("The given input data are invalid."),
    TRAVEL_CONN_ERROR("We had a connection problem, please retry soon."),
    TRAVEL_NO_ACTIVITY("You don't have any future activity."),
    TRAVEL_NO_EXISTING_ROUTE("There's no route for the path \"Start Place\"->\"Location\" that matches your preferences.");
    

    private final String message;

    ResponseTravelType(String s){
        this.message = s;
    }

    public String getMessage(){
        return this.message;
    }
}
