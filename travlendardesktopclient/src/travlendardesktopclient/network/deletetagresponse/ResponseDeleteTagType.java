/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient.network.deletetagresponse;

/**
 *
 * @author matteo
 */
public enum ResponseDeleteTagType {
    OK("ok"),
    DELETE_TAG_LOGIN_ERROR("The login data are invalid."),
    DELETE_TAG_WRONG_INPUT("The given input data are invalid."),
    DELETE_TAG_CONNECTION_ERROR("We had a connection problem, please retry soon.");
    private final String message;
    
    ResponseDeleteTagType(String s){
        this.message=s;
    }
    public String getMessage(){
        return message;
    }
}
