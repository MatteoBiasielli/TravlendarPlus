package biasiellicapodifatta.travlendar.user;

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
    @Override
    public String toString(){
        return tag;
    }
}
