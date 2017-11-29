/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient.network.deletetagresponse;

import java.util.ArrayList;
import travlendardesktopclient.data.user.FavouritePosition;


/**
 *
 * @author matteo
 */
public class ResponseDeleteTag {
    ResponseDeleteTagType rdtt;
    ArrayList<FavouritePosition> favPos;
    public ResponseDeleteTag(ResponseDeleteTagType respType,ArrayList<FavouritePosition> favPos){
        this.rdtt=respType;
        this.favPos=favPos;
    }
    public ResponseDeleteTagType getType(){
        return this.rdtt;
    }
    public ArrayList<FavouritePosition> getPositions(){
        return this.favPos;
    }
}
