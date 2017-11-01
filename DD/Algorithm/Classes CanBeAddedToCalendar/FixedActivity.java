package Travlendar;

import java.util.*;

public class FixedActivity implements Activity{
	private Date startDate;
	private Date endDate;
	
	
	public FixedActivity(Date s, Date e){
		startDate=s;
		endDate=e;
	}
	public FixedActivity(FixedActivity fa){
		this.startDate=new Date(fa.startDate.getTime());
		this.endDate=new Date(fa.endDate.getTime());
	}
	
	
	public boolean isBefore(FixedActivity a){
		return endDate.before(a.startDate) || endDate.equals(a.startDate);
	}
	public boolean isAfter(FixedActivity a){
		return this.startDate.after(a.endDate) || startDate.equals(a.endDate);
	}
	
	
	static FixedActivity parseFixedActivity(Break b, Date s, Date e) throws InvalidInputException{
		if(s.before(b.getStart()) || e.after(b.getEnd()))
			throw new InvalidInputException();
		return new FixedActivity(s,e);
	}
	
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

	public static ArrayList<FixedActivity> copyList(ArrayList<FixedActivity> fa){
		ArrayList<FixedActivity> ret=new ArrayList<>();
		for(FixedActivity act: fa)
			ret.add(new FixedActivity(act));
		return ret;
	}
}
