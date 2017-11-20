package travlendarplus.calendar;

import java.util.ArrayList;
import java.util.Date;

import travlendarplus.calendar.activities.*;
import travlendarplus.exceptions.*;

public class Calendar {
	/** Fixed Activities list**/
	private final ArrayList<FixedActivity> fixedActivities;
	/** breaks list **/
	private final ArrayList<Break> breaks;
	
	/**Written in minutes, SCATTO represents and must be equal to the minimum time
	 * granularity of the activities*/
	private static int SCATTO=15;
	
	/***********CONSTRUCTORS*****************/
	Calendar(){
		this.fixedActivities=new ArrayList<>();
		this.breaks=new ArrayList<>();
	}
	public Calendar(ArrayList<FixedActivity> fa, ArrayList<Break> b){
		this.fixedActivities=fa;
		this.breaks=b;
	}
	
	/******************************************/
	/**
	 * Adds a FixedActivity to the calendar
	 * @param a is the activity to be added
	 * @throws CannotBeAddedException is the activity can't be added
	 */
	public void addActivity(FixedActivity a) throws CannotBeAddedException{
		if(a.canBeAddedTo(this)){
			return;
		}
		throw new CannotBeAddedException("The activity cannot be added to the calendar");
	}
	/**
	 * Adds a Break to the calendar
	 * @param a is the break to be added
	 * @throws CannotBeAddedException is the activity can't be added
	 */
	public void addActivity(Break a) throws CannotBeAddedException{
		if(a.canBeAddedTo(this)){
			return;
		}
		throw new CannotBeAddedException("The activity cannot be added to the calendar");
	}
	
	/**
	 * Verifies if a list of FixedActivities is consinstent
	 * @param fa is the list of FixedActivities
	 * @return true if the list's elements don't overlap, false otherwise
	 */
	private boolean isConsistent(ArrayList<FixedActivity> fa){
		int size=fa.size();
		if(size<=1)
			return true;
		for(int i=0; i<size-1;i++)
			for(int j=i+1;j<size;j++)
				if(!fa.get(i).isBefore(fa.get(j)) && !fa.get(i).isAfter(fa.get(j)))
					return false;
		return true;
	}
	
	/**
	 * Verifies if a calendar containing the given activities can exist
	 * @param fa is the list of FixedActivities
	 * @param b is the list of Breaks
	 * @return true if the lists together can represent a calendar. false otherwise
	 */
	public boolean canBeACalendar(ArrayList<FixedActivity> fa, ArrayList<Break> b){
		return recursiveCanBeACalendar(fa,b,b.size());
	}
	
	/**
	 * Supports canBeACalendar 
	 * @param fa is the list of FixedActivities
	 * @param b is the list of Breaks
	 * @param s is the size of b
	 * @return true if the lists together can represent a calendar. false otherwise
	 */
	private boolean recursiveCanBeACalendar(ArrayList<FixedActivity> fa, ArrayList<Break> b, int s){
		if(s==0)
			return isConsistent(fa);
		Break bApp=b.get(s-1);
		Date start=bApp.getStartDate();
		Date end= new Date(start.getTime()+bApp.getDuration()*60*1000);
		while(true){
			try {
				FixedActivity faToAdd=FixedActivity.parseFixedActivity(bApp, start,end);
				fa.add(faToAdd);
				if(recursiveCanBeACalendar(fa, b, s-1))
					return true;
				start=new Date(start.getTime()+SCATTO*60*1000);
				end=new Date(end.getTime()+SCATTO*60*1000);
				fa.remove(faToAdd);
			} catch (InvalidInputException e) {
				break;
			}
			
		}
		return false;
	}
	
	/**@return a string human-readable version of the Object**/
        @Override
	public String toString(){
		String ris="FIXED:\n";
		for(FixedActivity fa:this.fixedActivities)
			ris+="("+fa.toString()+")\n";
		ris+="BREAKS:\n";
		for(Break fa:this.breaks)
			ris+="("+fa.toString()+")\n";
		return ris;
	}
        
        /** This method is used in the process of calculating the proper notification
         * to send to the user in case the activity he wants to insert will not allow him
         * to be on time to the activity itself or another activity.
         * The method creates a new calendar where fixed activities are extended to take 
         * into account also the estimated travel time.
         * @param c is the calendar to modify.
         * @return a new calendar where fixed activities are extended to take into account also the estimated travel time.
         */
        public static Calendar modifyCalendarWithEstimatedTravelTimes(Calendar c){
            ArrayList<FixedActivity> fa=c.getFixedActivities();
            ArrayList<FixedActivity> modified= new ArrayList<>();
            for(FixedActivity act:fa){
                FixedActivity mod=new FixedActivity(new Date(act.getStartDate().getTime()-act.getEstimatedTravelTime()*60*1000),act.getEndDate(),null,null,null,null,null);
                modified.add(mod);
            }
            ArrayList<Break> b= Break.copyList(c.getBreaks());
            return new Calendar(modified,b);
        }
	/*********************GETTERS**********************************/
	public ArrayList<FixedActivity> getFixedActivities(){
		return fixedActivities;
	}
	public ArrayList<Break> getBreaks(){
		return breaks;
	}
}
