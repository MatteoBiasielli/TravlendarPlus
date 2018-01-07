package biasiellicapodifatta.travlendar.data.user.preferences;

import android.content.res.Resources;

import java.util.ArrayList;

public class RangedPreference extends Preference{
	private RangedPreferenceType type;
	private int value;
        public RangedPreference(){
            super();
        }
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
	public boolean isRanged() {
            return true;
        }

	/* OTHER METHODS */
	public boolean equalTo(RangedPreference rp){
		Integer myType = this.type.getValue();
		Integer rpType = rp.getType().getValue();

		return myType.equals(rpType);
	}

	public RangedPreference getSameTypeIn(ArrayList<RangedPreference> rps) {
		for(RangedPreference rp : rps){
			if(this.equalTo(rp)){
				return rp;
			}
		}
		return null;
	}
}
