package it.polimi.ingsw.travlendarplus.user.preferences;

public class RangedPreference extends Preference{
	private RangedPreferenceType type;
	private int value;
	public RangedPreference(RangedPreferenceType t, int v){
		this.type=t;
		this.value=v;
	}
	
	
	/**@return a string human-readable version of the Object**/
	public String toString(){
		return type.name()+","+value;
	}
	
	/* GETTERS */
	public RangedPreferenceType getType(){
		return type;
	}
	public int getValue(){
		return value;
	}
}