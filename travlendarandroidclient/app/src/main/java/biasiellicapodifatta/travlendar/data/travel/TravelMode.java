package biasiellicapodifatta.travlendar.data.travel;

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
