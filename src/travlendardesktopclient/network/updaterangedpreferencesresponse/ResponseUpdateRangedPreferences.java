/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient.network.updaterangedpreferencesresponse;

import java.util.ArrayList;
import travlendardesktopclient.data.user.preferences.RangedPreference;



/**
 *
 * @author Emilio
 */
public class ResponseUpdateRangedPreferences {
    private final ResponseUpdateRangedPreferencesType rurpt;
    private final ArrayList<RangedPreference> rp;
    
    public ResponseUpdateRangedPreferences(ResponseUpdateRangedPreferencesType r){
        this.rurpt = r;
        this.rp = null;
    }
    
    public ResponseUpdateRangedPreferences(ResponseUpdateRangedPreferencesType r, ArrayList<RangedPreference> p){
        this.rurpt = r;
        this.rp = p;
    }
    
    public ResponseUpdateRangedPreferencesType getType(){
        return rurpt;
    }
    public ArrayList<RangedPreference> getPrefs(){
        return this.rp;
    }
}
