package Travlendar;

import java.util.*;

public class FixedActivity implements Activity{
	private Date startDate;
	private Date endDate;
	
	/*****************CONSTRUCTORS**********************/
	/**
	 * @param s is the start date
	 * @param e is the ending date
	 */
	public FixedActivity(Date s, Date e){
		startDate=s;
		endDate=e;
	}
	/**
	 * @param act is the FixedActivity to copy
	 */
	public FixedActivity(FixedActivity fa){
		this.startDate=new Date(fa.startDate.getTime());
		this.endDate=new Date(fa.endDate.getTime());
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
	
	/**
	 * This method is used to verify that breaks can have a placement in the calendar.
	 * @param b is the break to parse to FixedActivity.
	 * @param s is the starting time of the new FixedActivity.
	 * @param e is the ending time of the new FixedActivity.
	 * @return the new FixedActivity produced from break b and the given times.
	 * @throws InvalidInputException when s comes before b's starting time or
	 * 				e comes after b's ending time. Basically
	 * 				the exception is thrown when s and e are not coherent with the break
	 * 				that is going to be parsed.
	 */
	static FixedActivity parseFixedActivity(Break b, Date s, Date e) throws InvalidInputException{
		if(s.before(b.getStart()) || e.after(b.getEnd()))
			throw new InvalidInputException();
		return new FixedActivity(s,e);
	}	
	/**
	 * @param c is the calendar for which to verify whether the FixedActivity can
	 * be added into it or not.
	 * @return true if the calendar containing the FixedActivity to be added is
	 * still coherent, false otherwise.
	 */
	@Override
	public boolean canBeAddedTo(Calendar c) {
		ArrayList<FixedActivity> calendarActivities= c.getFixedActivities();
		for(FixedActivity fa:calendarActivities)
			if(!this.isBefore(fa) && !this.isAfter(fa))
				return false;
		ArrayList<FixedActivity> fixApp=new ArrayList<>();
		ArrayList<Break> breaks=new ArrayList<>();
		boundSubCalendar(c,fixApp, breaks);
		fixApp.add(this);
		return c.canBeACalendar(fixApp,breaks);
	}
	
	
	/**
	 * Supports canBeAddedTo method. This method reduces the complexity of the process
	 * followed to verify whether a FixedActivity can be added to a calendar or not
	 * by finding a sub-calendar of the original calendar on which to verify the conditions.
	 * Since the sub-calendar will probably contain less activities than the original calendar,
	 * operations that will be executed are significantly less.
	 * @param c is the original calendar
	 * @param fixApp is the ArrayList that, at the end of the process, will contain
	 * 			all the FixedActvities of the sub-calendar.
	 * @param breaksis the ArrayList that, at the end of the process, will contain
	 * 			all the Breaks of the sub-calendar.
	 */
	private void boundSubCalendar(Calendar c,ArrayList<FixedActivity> fixApp, ArrayList<Break> breaks) {
		ArrayList<FixedActivity> fa=c.getFixedActivities();
		ArrayList<Break> b=c.getBreaks();
		for(Break br:b){
			if((br.getStart().after(this.startDate) || br.getStart().equals(this.startDate))
					&& br.getStart().before(this.endDate) ||
					br.getEnd().after(this.startDate) && 
				(br.getEnd().before(this.endDate) || br.getEnd().equals(this.endDate))){
				breaks.add(br);
			}
		}
		for(FixedActivity fAct: fa){
			for(Break br:breaks){
				if((br.getStart().after(fAct.startDate) || br.getStart().equals(fAct.startDate))
						&& br.getStart().before(fAct.endDate) ||
						br.getEnd().after(fAct.startDate) && 
					(br.getEnd().before(fAct.endDate) || br.getEnd().equals(fAct.endDate))){
					fixApp.add(fAct);
					break;
				}
			}
		}
		fixApp=copyList(fixApp);
	}
	
	
	/**
	 * @param b is the array list of FixedActivity to copy
	 * @return a new ArrayList object containing a copy of all
	 * the FixedActivities in the ArrayList given as parameter
	 */
	public static ArrayList<FixedActivity> copyList(ArrayList<FixedActivity> fa){
		ArrayList<FixedActivity> ret=new ArrayList<>();
		for(FixedActivity act: fa)
			ret.add(new FixedActivity(act));
		return ret;
	}
}
