package it.polimi.ingsw.travlendarplus.travel;

public enum TravelMode {
	DRIVING("driving"), WALKING("walking"), BICYLING("bicycling"), TRANSIT("transit");
	private String mode;
	TravelMode(String s){
		this.mode=s;
	}
	public String getMode(){
		return this.mode;
	}
	
}
