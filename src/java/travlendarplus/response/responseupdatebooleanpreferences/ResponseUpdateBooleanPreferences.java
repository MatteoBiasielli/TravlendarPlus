/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responseupdatebooleanpreferences;

/**
 *
 * @author Emilio
 */
public class ResponseUpdateBooleanPreferences {
    private final ResponseUpdateBooleanPreferencesType rrt;
    public ResponseUpdateBooleanPreferences(ResponseUpdateBooleanPreferencesType r){
        this.rrt=r;
    }
    public ResponseUpdateBooleanPreferencesType getType(){
        return rrt;
    }
}
