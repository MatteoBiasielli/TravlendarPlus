package Travlendar;

import java.util.ArrayList;
import java.util.Date;

public class Calendar {
	private ArrayList<FixedActivity> fixedActivities;
	private ArrayList<Break> breaks;
	
	/**Written in minutes, SCATTO represents and must equal to the minimum time
	 * granularity of the activities*/
	private static int SCATTO=15;
	
	/***********CONSTRUCTORS*****************/
	Calendar(){
		this.fixedActivities=new ArrayList<>();
		this.breaks=new ArrayList<>();
	}
	Calendar(ArrayList<FixedActivity> fa, ArrayList<Break> b){
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
			fixedActivities.add(a);
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
			breaks.add(a);
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
		Date start=bApp.getStart();
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
	
	
	/*********************GETTERS**********************************/
	public ArrayList<FixedActivity> getFixedActivities(){
		return fixedActivities;
	}
	public ArrayList<Break> getBreaks(){
		return breaks;
	}
}
