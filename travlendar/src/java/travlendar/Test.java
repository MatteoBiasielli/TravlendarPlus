/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendar;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import travlendarplus.apimanager.APIManager;
import travlendarplus.calendar.Calendar;
import travlendarplus.calendar.activities.ActivityStatus;
import travlendarplus.calendar.activities.Break;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.CannotBeAddedException;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.InvalidPositionException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.notification.weather.Forecast;
import travlendarplus.user.User;

/**
 *
 * @author matteo
 */
public class Test {
    public static void main(String[] args) throws InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException, InvalidPositionException, CannotBeAddedException{
        /*User u=new User("cane","cane");
        String tag="CANION";
        DataLayer.deleteFavPosition(u, tag);*/
        /*User u=new User("cane", "cane");
        u.getCalendarFromDB();
        Break b=u.getCalendar().getBreaks().get(0);
        b.generateRequiredNotification(u.getCalendar());*/
        /*ArrayList<Forecast> f=APIManager.getYahooWeatherForcast("via caduti di marcinelle milano");
        for(Forecast fore:f)
            System.out.println(fore);*/
       // DataLayer.getNotification(new User("cane","cane"))-;
       
       //System.out.println(new Date(1531940600000L));
       //System.out.println(new Date(1531974600000L));
       //DataLayer.checkForNotification();
       User u=new User("a", "a");
       u.isValidLogin();
       u.getCalendarFromDB();
       Break b= new Break(new Date(1531987200000L + 6*60*60*1000), new Date(1531987200000L + 8*60*60*1000),"a","a","a","a",ActivityStatus.NOT_STARTED,30);
       u.getCalendar().addActivity(b);
       System.out.println(u.getCalendar());
       /*ArrayList<Integer> test= new ArrayList<>();
       ArrayList<ArrayList<Integer>> test2= new ArrayList<>();
       test.add(1);
       test.add(2);
       test2.add(new ArrayList<>());
       test2.get(0).add(11);
       test2.get(0).add(12);
       test2.add(new ArrayList<>());
       test2.get(1).add(21);
       test2.get(1).add(22);
       new Calendar(null,null).sortTimeSlotArrayLists(test, test2);
       for(int i=0;i<test.size();i++){
           System.out.println(test.get(i)+" "+ test2.get(i).get(0));
       }*/
    }
}
