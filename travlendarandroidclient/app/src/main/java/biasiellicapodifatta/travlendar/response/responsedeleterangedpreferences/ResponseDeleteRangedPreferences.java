package biasiellicapodifatta.travlendar.response.responsedeleterangedpreferences;

import java.util.ArrayList;

import biasiellicapodifatta.travlendar.data.user.preferences.RangedPreference;

/**
 *
 * @author Emilio
 */
public class ResponseDeleteRangedPreferences {
    private final ResponseDeleteRangedPreferencesType rdrpt;
    private final ArrayList<RangedPreference> rp;
    
    public ResponseDeleteRangedPreferences(ResponseDeleteRangedPreferencesType r){
        this.rdrpt = r;
        this.rp = null;
    }
    
    public ResponseDeleteRangedPreferences(ResponseDeleteRangedPreferencesType r, ArrayList<RangedPreference> p){
        this.rdrpt = r;
        this.rp = p;
    }

    public ArrayList<RangedPreference> getData(){
        return this.rp;
    }
    
    public ResponseDeleteRangedPreferencesType getType(){
        return rdrpt;
    }
}
