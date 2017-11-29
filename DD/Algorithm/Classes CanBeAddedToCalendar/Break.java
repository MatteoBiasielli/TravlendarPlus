package Travlendar;

import java.util.ArrayList;
import java.util.Date;

public class Break implements Activity{
	private Date startDate;
	private Date endDate;
	
	/**minutes**/
	private long duration;
	
	/****************CONSTRUCTORS*****************/
	/**
	 * @param s is the start date
	 * @param e is the ending date
	 * @param d is the duration
	 */
	public Break(Date s, Date e, long d){
		startDate=s;
		endDate=e;
		duration=d;
	}
	/**
	 * @param act is the break to copy
	 */
	public Break(Break act) {
		this.startDate=new Date(act.startDate.getTime());
		this.endDate=new Date(act.endDate.getTime());
		this.duration=act.duration;
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
	
	
	/*************GETTERS******************/
	public Date getStart(){
		return this.startDate;
	}
	public Date getEnd(){
		return this.endDate;
	}
	public long getDuration(){
		return duration;
	}	
}
