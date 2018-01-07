package biasiellicapodifatta.travlendar.response.responseupdatebooleanpreferences;

/**
 *
 * @author Emilio
 */
public enum ResponseUpdateBooleanPreferencesType {
    OK("ok"),
    UPDATE_BOOLEAN_PREFERENCES_INVALID_LOGIN("The given credentials are invalid."),
    UPDATE_BOOLEAN_PREFERENCES_WRONG_INPUT("The given input data are invalid."),
    UPDATE_BOOLEAN_PREFERENCES_CONNECTION_ERROR("We had a connection problem, please retry soon.");
    private final String message;
    
    ResponseUpdateBooleanPreferencesType(String s){
        this.message=s;
    }
    public String getMessage(){
        return message;
    }
}
