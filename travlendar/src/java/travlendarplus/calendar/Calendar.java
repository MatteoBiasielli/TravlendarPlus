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
	 *
	 */
	public void deleteActivity(FixedActivity a) throws NoSuchActivityException {
		FixedActivity toDelete = null;
		for(FixedActivity act : this.fixedActivities){
			if(act.getKey() == a.getKey()) {
				toDelete = act;
				break;
			}
		}
		if(toDelete != null)
			this.fixedActivities.remove(toDelete);
		else
			throw new NoSuchActivityException("The activity with id "+a.getKey()+" cannot be found.");
	}

	/**
	 *
	 */
	public void deleteActivity(Break a) throws NoSuchActivityException {
		Break toDelete = null;
		for(Break br : this.breaks){
			if(br.getKey() == a.getKey()){
				toDelete = br;
				break;
			}
		}
		if(toDelete != null)
			this.breaks.remove(toDelete);
		else
			throw new NoSuchActivityException("The activity with id" +a.getKey()+" cannot be found");
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
	
        private boolean breaksDontOverlap(ArrayList<Break> b){
		int size=b.size();
		if(size<=1)
			return true;
		for(int i=0; i<size-1;i++)
			for(int j=i+1;j<size;j++)
				if(!b.get(i).isBefore(b.get(j)) && !b.get(i).isAfter(b.get(j)))
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
		return !tooLongDurations(fa,b) && !oneBreakCompletelyIncludedInOneFixed(fa,b) && recursiveCanBeACalendar(fa,b,b.size());
	}
	
        
        public boolean canBeACalendarOptimized(ArrayList<FixedActivity> fa, ArrayList<Break> b){
            return isConsistent(fa) && breaksDontOverlap(b) && !tooLongDurations(fa,b) && breaksFit(fa,b);
        }
        
        private boolean breaksFit(ArrayList<FixedActivity> fa, ArrayList<Break> b){
            Date minStart;
            Date maxEnd;
            ArrayList<TimeSlot> slots=new ArrayList<>();
            if(b.isEmpty())
                return true;
            minStart=b.get(0).getStartDate();
            maxEnd=b.get(0).getEndDate();
            for(Break br:b){
                if(br.getStartDate().before(minStart))
                    minStart=br.getStartDate();
                if(br.getEndDate().after(maxEnd))
                    maxEnd=br.getEndDate();
            }
            fa.sort(null);
            if(!fa.isEmpty()){
                if(minStart.before(fa.get(0).getStartDate()))
                    setFreeTimeSlots(slots,minStart,fa.get(0).getStartDate());
                for(int i=0; i<fa.size()-1;i++){
                    setFreeTimeSlots(slots,fa.get(i).getEndDate(),fa.get(i+1).getStartDate());
                }
                if(maxEnd.after(fa.get(fa.size()-1).getEndDate()))
                    setFreeTimeSlots(slots,fa.get(0).getEndDate(),maxEnd);
            }else
                setFreeTimeSlots(slots,minStart,maxEnd);
            for(int i=0;i<b.size();i++){
                boolean done=false;
                slots.sort(null);
                for(int j=0;j<slots.size();j++){
                    if(slots.get(j).suitsToBreak(b.get(i))){
                        TimeSlot toRemove=slots.get(j);
                        slots.remove(j);
                        Date st;
                        Date en;
                        st=toRemove.getStart().before(b.get(i).getStartDate())?b.get(i).getStartDate():toRemove.getStart();
                        en=toRemove.getEnd().after(b.get(i).getEndDate())?b.get(i).getEndDate():toRemove.getEnd();
                        Date endAct=new Date(st.getTime()+b.get(i).getDuration()*60*1000);
                        slots.add(new TimeSlot(toRemove.getStart(),st));
                        slots.add(new TimeSlot(endAct,toRemove.getEnd()));
                        done=true;
                        break;
                    }
                }
                if(!done)
                    return false;
            }
            return true;
        }
        
        private void setFreeTimeSlots(ArrayList<TimeSlot> slots, Date s, Date e){
            slots.add(new TimeSlot(s,e));
        }
       
        private boolean tooLongDurations(ArrayList<FixedActivity> fa, ArrayList<Break> b){
            long acc=0;
            Date minStart;
            Date maxEnd;
            if(b.isEmpty())
                return false;
            minStart=b.get(0).getStartDate();
            maxEnd=b.get(0).getEndDate();
            for(Break br:b){
                acc-=br.getDuration()*60*1000;
                if(br.getStartDate().before(minStart))
                    minStart=br.getStartDate();
                if(br.getEndDate().after(maxEnd))
                    maxEnd=br.getEndDate();
            }
            acc+=maxEnd.getTime()-minStart.getTime();
            for(FixedActivity fact:fa){
                Date s=null;
                Date e=null;
                if(minStart.before(fact.getStartDate()))
                    s=fact.getStartDate();
                else
                    s=minStart;
                if(maxEnd.after(fact.getEndDate()))
                    e=fact.getEndDate();
                else
                    e=maxEnd;
                if(s.before(e))
                    acc-=e.getTime()-s.getTime();
            }
                
            return acc<0;
        }
        private boolean oneBreakCompletelyIncludedInOneFixed(ArrayList<FixedActivity> fa, ArrayList<Break> b){
            for(Break br:b)
                for(FixedActivity fact:fa)
                    if((br.getStartDate().equals(fact.getStartDate()) || br.getStartDate().after(fact.getStartDate()))
                            &&
                      (br.getEndDate().equals(fact.getEndDate()) || br.getEndDate().before(fact.getEndDate())))
                        return true;
            return false;
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

class TimeSlot implements Comparable{
    private Date s;
    private Date e;
    private int duration;
    protected TimeSlot(Date s, Date e){
        this.s=s;
        this.e=e;
        this.duration=(int)((e.getTime()-s.getTime())/(60*1000));
    }
    @Override
    public int compareTo(Object o){
        if(this.duration<((TimeSlot)o).duration)
            return -1;
        if(this.duration>((TimeSlot)o).duration)
            return 1;
        return 0;
    }
    public int getDur(){
        return this.duration;
    }
    public Date getStart(){
        return this.s;
    }
    public Date getEnd(){
        return this.e;
    }
    public boolean suitsToBreak(Break b){
        Date st;
        Date en;
        st=s.before(b.getStartDate())?b.getStartDate():s;
        en=e.after(b.getEndDate())?b.getEndDate():e;
        int dur=(int)((en.getTime()-st.getTime())/(60*1000));
        return dur>=b.getDuration() && (b.contains(s) || b.contains(e) || b.isContainedIn(s,e));
    }
}