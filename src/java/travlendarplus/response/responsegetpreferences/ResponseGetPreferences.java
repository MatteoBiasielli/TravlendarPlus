/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responsegetpreferences;

import java.util.ArrayList;
import travlendarplus.user.preferences.Preference;

/**
 *
 * @author Emilio
 */
public class ResponseGetPreferences {
    private final ResponseGetPreferencesType rpt;
    private final ArrayList<Preference> rbp;
    
    public ResponseGetPreferences(ResponseGetPreferencesType r){
        this.rpt = r;
        this.rbp = null;
    }
    
    public ResponseGetPreferences(ResponseGetPreferencesType r, ArrayList<Preference> bp){
        this.rpt = r;
        this.rbp = bp;
    }
    
    public ArrayList<Preference> getPreferences(){
        return rbp;
    }
    
    public ResponseGetPreferencesType getType(){
        return rpt;
    }
}
