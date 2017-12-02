package biasiellicapodifatta.travlendar.response.responsegetpreferences;


import biasiellicapodifatta.travlendar.data.user.User;

/**
 *
 * @author Emilio
 */
public class ResponseGetPreferences {
    private final ResponseGetPreferencesType rpt;
    private final User rbp;
    
    public ResponseGetPreferences(ResponseGetPreferencesType r){
        this.rpt = r;
        this.rbp = null;
    }
    
    public ResponseGetPreferences(ResponseGetPreferencesType r, User bp){
        this.rpt = r;
        this.rbp = bp;
    }
    
    public User getUser(){
        return rbp;
    }
    
    public ResponseGetPreferencesType getType(){
        return rpt;
    }
}
