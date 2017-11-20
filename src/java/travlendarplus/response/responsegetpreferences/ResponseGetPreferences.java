/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responsegetpreferences;

/**
 *
 * @author Emilio
 */
public class ResponseGetPreferences {
    private final ResponseGetPreferencesType rrt;
    public ResponseGetPreferences(ResponseGetPreferencesType r){
        this.rrt=r;
    }
    public ResponseGetPreferencesType getType(){
        return rrt;
    }
}
