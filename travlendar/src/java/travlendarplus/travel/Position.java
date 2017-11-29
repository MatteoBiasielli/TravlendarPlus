package travlendarplus.travel;

import java.io.IOException;

import travlendarplus.apimanager.APIManager;
import travlendarplus.exceptions.InvalidPositionException;

public class Position {
	private double latitude;
	private double longitude;
	public Position(double lat, double lon){
		this.latitude=lat;
		this.longitude=lon;
	}
	public double getLatitude(){
		return this.latitude;
	}
	public double getLongitude(){
		return this.longitude;
	}
	
	/**@return a string human-readable version of the Object**/
	public String toString(){
		return "("+this.latitude+","+this.longitude+")";
	}
	public String convertToFormattedAddress() throws IOException, InvalidPositionException{
		return APIManager.googleReverseGeocodingRequest(this);
	}
}
