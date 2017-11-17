package travlendarplus.user.preferences;

public class BooleanPreferencesSet extends Preference{
	private boolean personalCar;
	private boolean carSharing;
	private boolean personalBike;
	private boolean bikeSharing;
	private boolean publicTransport;
	private boolean uberTaxi;
	private Modality mode;
	
	public BooleanPreferencesSet(boolean pC, boolean cS, boolean pB, boolean bS, boolean pT, boolean uT, Modality m){
		this.personalCar=pC;
		this.carSharing=cS;
		this.personalBike=pB;
		this.bikeSharing=bS;
		this.publicTransport=pT;
		this.uberTaxi=uT;
		this.mode=m;
	}
	
	
	/**@return a string human-readable version of the Object**/
	public String toString(){
		return personalCar+","+carSharing+","+personalBike+","+bikeSharing+","+publicTransport+","+ uberTaxi+","+mode.name();
	}
	
	/*GETTERS*/
	public boolean personalCar(){
		return this.personalCar;
	}
	public boolean carSharing(){
		return this.carSharing;
	}
	public boolean personalBike(){
		return this.personalBike;
	}
	public boolean bikeSharing(){
		return this.bikeSharing;
	}
	public boolean publicTrasport(){
		return this.publicTransport;
	}
	public boolean uberTaxi(){
		return this.uberTaxi;
	}
	public Modality mode(){
		return this.mode;
	}
	
}
