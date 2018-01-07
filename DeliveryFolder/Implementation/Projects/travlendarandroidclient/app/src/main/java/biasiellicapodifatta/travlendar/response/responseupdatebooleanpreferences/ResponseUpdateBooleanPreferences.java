package biasiellicapodifatta.travlendar.response.responseupdatebooleanpreferences;

import biasiellicapodifatta.travlendar.data.user.preferences.BooleanPreferencesSet;

/**
 *
 * @author Emilio
 */
public class ResponseUpdateBooleanPreferences {
    private final ResponseUpdateBooleanPreferencesType rubpt;
    private final BooleanPreferencesSet rbp;
    
    public ResponseUpdateBooleanPreferences(ResponseUpdateBooleanPreferencesType r){
        this.rubpt = r;
        this.rbp = null;
    }
    
    public ResponseUpdateBooleanPreferences(ResponseUpdateBooleanPreferencesType r, BooleanPreferencesSet bp){
        this.rubpt = r;
        this.rbp = bp;
    }

    public BooleanPreferencesSet getData(){
        return this.rbp;
    }

    public ResponseUpdateBooleanPreferencesType getType(){
        return rubpt;
    }
}
