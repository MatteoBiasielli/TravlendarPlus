/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responsegetpreferences;

/**
 *
 * @author Emilio
 */
public enum ResponseGetPreferencesType {
    OK("ok"),
    GET_PREFERENCES_INVALID_LOGIN("The given credentials are invalid."),
    GET_PREFERENCES_WRONG_INPUT("The given input data are invalid."),
    GET_PREFERENCES_CONNECTION_ERROR("We had a connection problem, please retry soon.");
    private final String message;
    
    ResponseGetPreferencesType(String s){
        this.message=s;
    }
    public String getMessage(){
        return message;
    }
}
