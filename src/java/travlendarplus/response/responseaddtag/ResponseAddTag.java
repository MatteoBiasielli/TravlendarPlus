/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responseaddtag;

import java.util.ArrayList;
import travlendarplus.user.FavouritePosition;

/**
 *
 * @author matteo
 */
public class ResponseAddTag {
    ResponseAddTagType ratt;
    ArrayList<FavouritePosition> favPos;
    public ResponseAddTag(ResponseAddTagType respType,ArrayList<FavouritePosition> favPos){
        this.ratt=respType;
        this.favPos=favPos;
    }
    public ResponseAddTagType getType(){
        return this.ratt;
    }
    public ArrayList<FavouritePosition> getPositions(){
        return this.favPos;
    }
}
