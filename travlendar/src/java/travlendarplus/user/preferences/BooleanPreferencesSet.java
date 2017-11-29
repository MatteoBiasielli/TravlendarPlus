package travlendarplus.user.preferences;

import travlendarplus.travel.Route;

public class BooleanPreferencesSet extends Preference{
	private final boolean personalCar;
	private final boolean carSharing;
	private final boolean personalBike;
	private final boolean bikeSharing;
	private final boolean publicTransport;
	private final boolean uberTaxi;
	private final Modality mode;
	
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
	
        @Override
        public boolean isRespectedBy(Route r) {
            return true;
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

        @Override
        public boolean isRanged() {
            return false;
        }

        
	
}
