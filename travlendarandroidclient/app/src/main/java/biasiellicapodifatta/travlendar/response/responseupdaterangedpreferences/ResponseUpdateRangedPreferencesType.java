package biasiellicapodifatta.travlendar.response.responseupdaterangedpreferences;

/**
 *
 * @author Emilio
 */
public enum ResponseUpdateRangedPreferencesType {
    OK("ok"),
    UPDATE_RANGED_PREFERENCES_INVALID_LOGIN("The given credentials are invalid."),
    UPDATE_RANGED_PREFERENCES_WRONG_INPUT("The given input data are invalid."),
    UPDATE_RANGED_PREFERENCES_CONNECTION_ERROR("We had a connection problem, please retry soon.");
    private final String message;
    
    ResponseUpdateRangedPreferencesType(String s){
        this.message=s;
    }
    public String getMessage(){
        return message;
    }
}
