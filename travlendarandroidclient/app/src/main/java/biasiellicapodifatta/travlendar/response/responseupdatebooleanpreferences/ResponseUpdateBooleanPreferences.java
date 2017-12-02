/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biasiellicapodifatta.travlendar.response.responseupdatebooleanpreferences;

import biasiellicapodifatta.travlendar.user.preferences.BooleanPreferencesSet;

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
    
    public ResponseUpdateBooleanPreferencesType getType(){
        return rubpt;
    }
}
