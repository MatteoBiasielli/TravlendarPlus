package Travlendar;

import java.util.ArrayList;
import java.util.Date;

public class Break implements Activity{
	private Date startDate;
	private Date endDate;
	
	/**minutes**/
	private long duration;

	public Break(Break act) {
		this.startDate=new Date(act.startDate.getTime());
		this.endDate=new Date(act.endDate.getTime());
		this.duration=act.duration;
	}

	@Override
	public boolean canBeAddedTo(Calendar c) {
		ArrayList<Break> b=Break.copyList(c.getBreaks());
		b.add(new Break(this));
		ArrayList<FixedActivity> fa=FixedActivity.copyList(c.getFixedActivities());
		return c.canBeACalendar(fa, b);
	}
	
	/**GETTERS**/
	public Date getStart(){
		return this.startDate;
	}
	public Date getEnd(){
		return this.endDate;
	}
	public long getDuration(){
		return duration;
	}
	public static ArrayList<Break> copyList(ArrayList<Break> b){
		ArrayList<Break> ret=new ArrayList<>();
		for(Break act: b)
			ret.add(new Break(act));
		return ret;
	}
}
