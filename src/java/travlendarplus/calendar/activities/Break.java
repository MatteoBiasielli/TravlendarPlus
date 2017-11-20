package travlendarplus.calendar.activities;

import java.util.ArrayList;
import java.util.Date;

import travlendarplus.calendar.Calendar;
import travlendarplus.response.responseaddactivity.ResponseAddActivityNotification;
import travlendarplus.user.User;

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
	
	/**
	 * @param c is the calendar for which to verify whether the break can
	 * be added into it or not.
	 * @return true if the calendar containing the break to be added is
	 * still coherent, false otherwise.
	 */
	@Override
	public boolean canBeAddedTo(Calendar c) {
		ArrayList<Break> b=Break.copyList(c.getBreaks());
		b.add(new Break(this));
		ArrayList<FixedActivity> fa=FixedActivity.copyList(c.getFixedActivities());
		return c.canBeACalendar(fa, b);
	}
	
	/**
	 * @param b is the array list of breaks to copy
	 * @return a new ArrayList object containing a copy of all
	 * the breaks in the ArrayList given as parameter
	 */
	public static ArrayList<Break> copyList(ArrayList<Break> b){
		ArrayList<Break> ret=new ArrayList<>();
		for(Break act: b)
			ret.add(new Break(act));
		return ret;
	}
	
        /**Should calculate the esitmated travel time for the activity.
         * Since breaks don't support/need this, it just returns 0.
         * @param tagStart is the starting location tag
         * @param tagLoc is the activity location tag
         * @param u is the user for which the calulus has to be performed
         * @return 0, always. 
         */
        @Override
        public int calculateEstimatedTravelTime(String tagStart, String tagLoc, User u){
            return 0;
        }
        
        /**
         * Computes the notification that would be generated when this activity
         * is added to the calendar given as parameter.
         * @param c the caledar mentioned above.
         * @return the notification that would be generated when this activity 
         * is added to the calendar given as parameter.
         */
        @Override
        public ResponseAddActivityNotification generateRequiredNotification(Calendar c) {
            Calendar mod= Calendar.modifyCalendarWithEstimatedTravelTimes(c);
            if(!this.canBeAddedTo(mod))
                return ResponseAddActivityNotification.OTHER;
            return ResponseAddActivityNotification.NO;
        }
        
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
