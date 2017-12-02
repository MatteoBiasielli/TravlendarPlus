package biasiellicapodifatta.travlendar.data.activities;

import java.util.ArrayList;
import java.util.Date;

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
        
	/*********************GETTERS**********************************/
	public ArrayList<FixedActivity> getFixedActivities(){
		return fixedActivities;
	}
	public ArrayList<Break> getBreaks(){
		return breaks;
	}
}
