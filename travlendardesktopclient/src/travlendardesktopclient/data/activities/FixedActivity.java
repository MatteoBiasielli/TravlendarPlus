package travlendardesktopclient.data.activities;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


public class FixedActivity extends Activity{
    
	/**MINUTES*/
	private int estimatedTravelTime;
	/* ****************CONSTRUCTORS**********************/
	/**
	 * @param s is the start date
	 * @param e is the ending date
	 */
	public FixedActivity(Date s, Date e,String l, String n, String lA, String sP, ActivityStatus actSt){
		super(s,e,l,n,lA,sP,actSt);
                this.estimatedTravelTime=0;
	}
	/**
	 * @param fa is the FixedActivity to copy
	 */
	public FixedActivity(FixedActivity fa){
		this.startDate=new Date(fa.startDate.getTime());
		this.endDate=new Date(fa.endDate.getTime());
		this.label=fa.label==null?null:new String(fa.label);
		this.notes=fa.notes==null?null:new String(fa.notes);
		this.locationAddress=fa.locationAddress==null?null:new String(fa.locationAddress);
		this.startPlaceAddress=fa.startPlaceAddress==null?null:new String(fa.startPlaceAddress);
		this.actStatus=fa.actStatus;
		this.key=fa.key;
		this.keySet=fa.keySet;
                this.estimatedTravelTime=fa.estimatedTravelTime;
	}

	/**
	 *
	 */
	public FixedActivity(){
		key = 0;
		keySet = false;
	}
	
	/*************************************************/
	
	/**
	 * @param a is a fixed activity to compare with the caller object
	 * @return true if the callers's ending time comes before or is equal to
	 * the parameters's starting time. false otherwise
	 */
	public boolean isBefore(FixedActivity a){
		return endDate.before(a.startDate) || endDate.equals(a.startDate);
	}
	
	/**
	 * @param a is a fixed activity to compare with the caller object
	 * @return true if the callers's starting time comes after or is equal to
	 * the parameters's ending time. false otherwise
	 */
	public boolean isAfter(FixedActivity a){
		return this.startDate.after(a.endDate) || startDate.equals(a.endDate);
	}
	
	/* GETTERS */
        @Override
	public boolean isFlexible(){
		return false;
	}
        @Override
	public long getDuration(){
		return 0;
	}
        @Override
        public int getEstimatedTravelTime(){
            return this.estimatedTravelTime;
        }

        

        
}
