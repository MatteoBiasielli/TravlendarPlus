package biasiellicapodifatta.travlendar.response.responsedeleterangedpreferences;

/**
 *
 * @author Emilio
 */
public enum ResponseDeleteRangedPreferencesType {
    OK("ok"),
    DELETE_RANGED_PREFERENCES_INVALID_LOGIN("The given credentials are invalid."),
    DELETE_RANGED_PREFERENCES_WRONG_INPUT("The given input data are invalid."),
    DELETE_RANGED_PREFERENCES_CONNECTION_ERROR("We had a connection problem, please retry soon.");
    private final String message;
    
    ResponseDeleteRangedPreferencesType(String s){
        this.message=s;
    }
    public String getMessage(){
        return message;
    }
}
