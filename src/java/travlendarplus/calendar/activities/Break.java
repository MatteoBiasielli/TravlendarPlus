package travlendarplus.calendar.activities;

import java.util.ArrayList;
import java.util.Date;

import travlendarplus.calendar.Calendar;

public class Break extends Activity{
	
	/**minutes**/
	private long duration;
	
	/****************CONSTRUCTORS*****************/
	/**
	 * @param s is the start date
	 * @param e is the ending date
	 * @param d is the duration
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
		this.label=new String(act.label);
		this.notes=new String(act.notes);
		this.locationAddress=new String(act.locationAddress);
		this.startPlaceAddress=new String(act.startPlaceAddress);
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
	
	/**@return a string human-readable version of the Object**/
	public String toString(){
		return super.toString()+", DUR="+duration+"m";
	}
	/* ************GETTERS******************/
	public long getDuration(){
		return duration;
	}	
	public boolean isFlexible(){
		return true;
	}
}
