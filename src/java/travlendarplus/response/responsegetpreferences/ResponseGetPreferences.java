/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responsegetpreferences;


import travlendarplus.user.User;

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
