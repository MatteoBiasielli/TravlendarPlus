// MATTEO BIASIELLI, EMILIO CAPO, MATTIA DI FATTA
/* INITIAL COMMENT */
/* This model is based on the UML class diagram showed in the RASD file.
Our intention is to represent the system with the imposed constrants,
described in the above mentioned file, and to show that it is consistent and 
that admits some possible worlds which can represent real situations.
*/

/*DIFFERENCES WITH THE UML DIAGRAM
One of the main differences with the UML diagram is that here we will not 
model dates as a triple <day, month, year> for two reasons:
-it's not strictly necessary to show what we want to show;
-it's easier to use normal integers on Alloy.
In spite of this, we will model days as simple positive integers.
Activities' start and end time will be modeled as integers included in [0,24),
assuming that activities' duration can be just an integer number of hours.
*/

open util/integer
sig User{
	calendar: one Calendar
}
sig Calendar{
	activities: set Activity
}
sig Activity{
	startDay: one Int,
	endDay: one Int,
	startTime: one Int,
	endTime: one Int
}{
	/* The Activity start time must be prior to the end time. 
	Same with the start day and end day*/
	startDay>=0 && endDay>=startDay && 
	(startTime<endTime || endDay>startDay) &&
	startTime>=0 && startTime<24 && endTime>=0 && endTime<24
}

/*The relationshp between users and calendars is one-to-one */
fact notSharedCalendars{
	all disj u1,u2: User | u1.calendar!=u2.calendar
}
/*different calendars don't contain the same activities*/
fact notSharedActivities{
	all disj c1,c2: Calendar | no (c1.activities & c2.activities)
}


/*all the activities are associated to a calendar*/
/*
fact noActivitiesNotAssociated{
	all a1: Activity| some c1:Calendar| a1 in c1.activities
}
*/
/*all the calendars are associated to a user*/
/*
fact noCalendarsNotAssociated{
	all c1: Calendar| some u1:User| c1 in u1.calendar
}
*


/*in a certain calendar there are no overlapping activities, since a user can't be physically in two different activities at the same time.
This happens when, comparing all the possible couples of activities, the end day of one comes before the start day of the other one.
If the two activities are scheduled on the same day, the end time of one must come before the start time of the other one*/
fact noOverlappingActivitiesOnAUser{
	all c:Calendar| all disj a1, a2:c.activities | (a1.endDay=a2.startDay && a1.endTime<=a2.startTime || a2.endTime<=a1.startTime)
																	 ||  a1.endDay<a2.startDay || a2.endDay<a1.startDay
}

pred show{
	some c:Calendar,a:Activity | deleteActivityFromCalendar[c,a]
	#User=2
	#Activity=6
}
//run show for 6 but 6 Int

/*delete an activity from a calendar*/
pred deleteActivityFromCalendar[c:Calendar, a:Activity]{
	c.activities=c.activities-a	
}

/*Add an activity to a calendar. */
pred addActivityToCalendar[c:Calendar, a:Activity]{
	c.activities=c.activities+a
}

/* Tells when a calendar is consistent 
	we consider a calendar to be consistent when the activities it contains don't overlap among
	themselves and when its activities (speaking about objects) are not contained into other calendars.
*/
pred calendarIsConsistent[c:Calendar]{
	 (all disj a1, a2:c.activities | (a1.endDay=a2.startDay && a1.endTime<=a2.startTime || a2.endTime<=a1.startTime)
																	 ||  a1.endDay<a2.startDay || a2.endDay<a1.startDay)
	&&
	(all c1:Calendar| c.activities & c1.activities!=none => c=c1)
}
/* Due to the facts expressed above, the only possible worlds will be the
only consistent ones that stay consistent even after adding an activity to a calendar 
COMMENT THE noActivitiesNotAssociated FACT TO TEST THIS, OTHERWISE THERE WILL BE NO FREE ACTIVITIES
TO ADD TO A CALENDAR*/
assert addIsConsistent{
	all c:Calendar, a:Activity | addActivityToCalendar[c,a] implies calendarIsConsistent[c]
}
//check addIsConsistent for 10 but 6 Int
