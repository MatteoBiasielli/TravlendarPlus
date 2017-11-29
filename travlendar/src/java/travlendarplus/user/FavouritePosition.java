/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.user;

/**
 *
 * @author matteo
 */
public class FavouritePosition {
    private String address;
    private String tag;
    public FavouritePosition(String addr, String tag){
        this.address=addr;
        this.tag=tag;
    }
    public String getAddress(){
        return this.address;
    }
    public String getTag(){
        return this.tag;
    }
}
