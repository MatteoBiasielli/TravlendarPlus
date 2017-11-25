/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responsedeleterangedpreferences;

import travlendarplus.user.preferences.RangedPreference;

/**
 *
 * @author Emilio
 */
public class ResponseDeleteRangedPreferences {
    private final ResponseDeleteRangedPreferencesType rdrpt;
    private final RangedPreference[] rp;
    
    public ResponseDeleteRangedPreferences(ResponseDeleteRangedPreferencesType r){
        this.rdrpt = r;
        this.rp = null;
    }
    
    public ResponseDeleteRangedPreferences(ResponseDeleteRangedPreferencesType r, RangedPreference[] p){
        this.rdrpt = r;
        this.rp = p;
    }
    
    public ResponseDeleteRangedPreferencesType getType(){
        return rdrpt;
    }
}
