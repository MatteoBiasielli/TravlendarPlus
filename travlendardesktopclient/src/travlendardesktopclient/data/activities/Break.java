package travlendardesktopclient.data.activities;


import java.util.Date;


public class Break extends Activity{
	
	/**minutes**/
	private long duration;
	
	/****************CONSTRUCTORS*****************/
	/**
	 * @param s is the start date
	 * @param e is the ending date
         * @param l is the label
         * @param n is the notes
	 * @param d is the duration
         * @param lA is the location address. this is not considered in breaks
         * @param sP is the start place address. this is not considered in breaks
         * @param actSt is the activity status
	 */
	public Break(Date s, Date e,String l, String n, String lA, String sP, ActivityStatus actSt, long d){
		super(s,e,l,n,lA,sP,actSt);
		duration=d;
	}
	/**
	 * @param act is the break to copy
	 */
	public Break(Break act) {
		this.startDate=new Date(act.startDate.getTime());
		this.endDate=new Date(act.endDate.getTime());
		this.duration=act.duration;
                this.label=act.label==null?null:new String(act.label);
		this.notes=act.notes==null?null:new String(act.notes);
		this.locationAddress=act.locationAddress==null?null:new String(act.locationAddress);
		this.startPlaceAddress=act.startPlaceAddress==null?null:new String(act.startPlaceAddress);
		this.actStatus=act.actStatus;
		this.key=act.key;
		this.keySet=act.keySet;
	}
	/*************************************************/
	
        
	/**@return a string human-readable version of the Object**/
        @Override
	public String toString(){
		return super.toString()+", DUR="+duration+"m";
	}
	/* ************GETTERS******************/
        @Override
	public long getDuration(){
		return duration;
	}	
        @Override
	public boolean isFlexible(){
		return true;
	}
        @Override
        public int getEstimatedTravelTime(){
            return 0;
        }

    
}
