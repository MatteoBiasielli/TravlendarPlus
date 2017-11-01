package Travlendar;

import java.util.ArrayList;
import java.util.Date;

public class Calendar {
	private ArrayList<FixedActivity> fixedActivities;
	private ArrayList<Break> breaks;
	/***minutes***/
	private static int SCATTO=15;
	Calendar(){
		this.fixedActivities=new ArrayList<>();
		this.breaks=new ArrayList<>();
	}
	Calendar(ArrayList<FixedActivity> fa, ArrayList<Break> b){
		this.fixedActivities=fa;
		this.breaks=b;
	}
	public void addActivity(FixedActivity a) throws CannotBeAddedException{
		if(a.canBeAddedTo(this))
			fixedActivities.add(a);
		throw new CannotBeAddedException("The activity cannot be added to the calendar");
	}
	public void addActivity(Break a) throws CannotBeAddedException{
		if(a.canBeAddedTo(this))
			breaks.add(a);
		throw new CannotBeAddedException("The activity cannot be added to the calendar");
	}
	
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
	
	public boolean canBeACalendar(ArrayList<FixedActivity> fa, ArrayList<Break> b){
		return recursiveCanBeACalendar(fa,b,b.size());
	}
	private boolean recursiveCanBeACalendar(ArrayList<FixedActivity> fa, ArrayList<Break> b, int s){
		if(s==0)
			return isConsistent(fa);
		Break bApp=b.get(s-1);
		Date start=bApp.getStart();
		Date end= new Date(start.getTime()+bApp.getDuration());
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
	/***GETTERS***/
	public ArrayList<FixedActivity> getFixedActivities(){
		return fixedActivities;
	}
	public ArrayList<Break> getBreaks(){
		return breaks;
	}
}
