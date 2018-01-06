/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.calendar;

import java.util.ArrayList;
import java.util.Date;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import travlendarplus.calendar.activities.Break;
import travlendarplus.calendar.activities.FixedActivity;
import travlendarplus.exceptions.CannotBeAddedException;
import travlendarplus.exceptions.NoSuchActivityException;

/**
 *
 * @author matteo
 */
public class TestCalendar {
    private Calendar testCal=new Calendar();
    
    @Test(expected=travlendarplus.exceptions.CannotBeAddedException.class)
    public void testCanBeAddedToFixed() throws CannotBeAddedException{
        FixedActivity fa=new FixedActivity(new Date(0), new Date(1000),null,null,null,null,null);
        testCal.addActivity(fa);
        testCal.getFixedActivities().add(fa);
        assertTrue(true);
        Break b=new Break(new Date(0), new Date(61000),null,null,null,null,null,1);
        testCal.addActivity(b);
        testCal.getBreaks().add(b);
        assertTrue(true);
        testCal.addActivity(fa);
        assertTrue(false);
    }
    @Test(expected=travlendarplus.exceptions.CannotBeAddedException.class)
    public void testCanBeAddedToBreak() throws CannotBeAddedException{
        FixedActivity fa=new FixedActivity(new Date(0), new Date(1000),null,null,null,null,null);
        testCal.addActivity(fa);
        testCal.getFixedActivities().add(fa);
        assertTrue(true);
        Break b=new Break(new Date(0), new Date(61000),null,null,null,null,null,1);
        testCal.addActivity(b);
        testCal.getBreaks().add(b);
        assertTrue(true);
        testCal.addActivity(b);
        assertTrue(false);
    }
    
    @Test(expected=travlendarplus.exceptions.NoSuchActivityException.class)
    public void testDeleteActivityFixed() throws NoSuchActivityException{
        FixedActivity fa=new FixedActivity(new Date(0), new Date(1000),null,null,null,null,null);
        fa.setKey(1);
        testCal.getFixedActivities().add(fa);
        testCal.deleteActivity(fa);
        assertTrue(true);
        testCal.deleteActivity(fa);
    }
    @Test(expected=travlendarplus.exceptions.NoSuchActivityException.class)
    public void testDeleteActivityBreak() throws NoSuchActivityException{
        Break b=new Break(new Date(0), new Date(61000),null,null,null,null,null,1);
        b.setKey(1);
        testCal.getBreaks().add(b);
        testCal.deleteActivity(b);
        assertTrue(true);
        testCal.deleteActivity(b);
    }
    
    @Test
    public void testCanBeACalendar(){
        ArrayList<FixedActivity> fas=new ArrayList<>();
        ArrayList<Break> br= new ArrayList<>();
        FixedActivity fa=new FixedActivity(new Date(0), new Date(900000),null,null,null,null,null);
        Break b=new Break(new Date(0), new Date(1800000),null,null,null,null,null,15);
        fas.add(fa);
        br.add(b);
        assertTrue(testCal.canBeACalendar(fas, br));
        FixedActivity fa2=new FixedActivity(new Date(0), new Date(900000),null,null,null,null,null);
        fas.add(fa2);
        assertFalse(testCal.canBeACalendar(fas, br));
        fas.remove(fa2);
        Break b2=new Break(new Date(0), new Date(2700000),null,null,null,null,null,30);
        br.add(b2);
        assertFalse(testCal.canBeACalendar(fas, br));
    }
    @Test
    public void testCanBeACalendarOptimized(){
        ArrayList<FixedActivity> fas=new ArrayList<>();
        ArrayList<Break> br= new ArrayList<>();
        FixedActivity fa=new FixedActivity(new Date(0), new Date(900000),null,null,null,null,null);
        Break b=new Break(new Date(0), new Date(1800000),null,null,null,null,null,15);
        fas.add(fa);
        br.add(b);
        assertTrue(testCal.canBeACalendarOptimized(fas, br));
        FixedActivity fa2=new FixedActivity(new Date(0), new Date(900000),null,null,null,null,null);
        fas.add(fa2);
        assertFalse(testCal.canBeACalendarOptimized(fas, br));
        fas.remove(fa2);
        Break b2=new Break(new Date(0), new Date(2700000),null,null,null,null,null,30);
        br.add(b2);
        assertFalse(testCal.canBeACalendarOptimized(fas, br));
    }
}
