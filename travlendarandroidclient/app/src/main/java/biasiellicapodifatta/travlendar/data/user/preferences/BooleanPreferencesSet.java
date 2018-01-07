package biasiellicapodifatta.travlendar.data.user.preferences;

public class BooleanPreferencesSet extends Preference{
	private boolean personalCar;
	private boolean carSharing;
	private boolean personalBike;
	private boolean bikeSharing;
	private boolean publicTransport;
	private boolean uberTaxi;
	private Modality mode;
	
	public BooleanPreferencesSet(){
		this.personalCar=true;
		this.carSharing=true;
		this.personalBike=true;
		this.bikeSharing=true;
		this.publicTransport=true;
		this.uberTaxi=true;
		this.mode=Modality.MINIMIZE_TIME;
        }

	public BooleanPreferencesSet(boolean pC, boolean cS, boolean pB, boolean bS, boolean pT, boolean uT, Modality m){
		this.personalCar=pC;
		this.carSharing=cS;
		this.personalBike=pB;
		this.bikeSharing=bS;
		this.publicTransport=pT;
		this.uberTaxi=uT;
		this.mode=m;
	}

	public BooleanPreferencesSet(BooleanPreferencesSet copySet){
		this.personalCar=copySet.personalCar();
		this.carSharing=copySet.carSharing();
		this.personalBike=copySet.personalBike();
		this.bikeSharing=copySet.bikeSharing();
		this.publicTransport=copySet.publicTrasport();
		this.uberTaxi=copySet.uberTaxi();
		this.mode=copySet.mode();
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

	/*SETTERS*/
	public void setPersonalCar(boolean pC){ this.personalCar = pC; }
	public void setCarSharing(boolean cS){ this.carSharing = cS; }
	public void setPersonalBike(boolean pB){ this.personalBike = pB; }
	public void setBikeSharing(boolean bS){ this.bikeSharing = bS; }
	public void setPublicTransport(boolean pT){ this.publicTransport = pT; }
	public void setUberTaxi(boolean uT){ this.uberTaxi = uT; }
	public void setMode(Modality m){ this.mode = m; }

	public boolean isRanged() {
            return false;
        }
	public boolean equalTo(BooleanPreferencesSet bs){
		return this.personalCar == bs.personalCar
				&& this.carSharing == bs.carSharing
				&& this.personalBike == bs.personalBike
				&& this.bikeSharing == bs.bikeSharing
				&& this.publicTransport == bs.publicTransport
				&& this.uberTaxi == bs.uberTaxi
				&& this.mode == bs.mode;
	}
}
