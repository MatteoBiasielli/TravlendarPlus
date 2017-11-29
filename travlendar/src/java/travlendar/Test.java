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
import travlendarplus.calendar.activities.Break;
import travlendarplus.data.DataLayer;
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
    public static void main(String[] args) throws InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException, InvalidPositionException{
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
       // DataLayer.getNotification(new User("cane","cane"));
       System.out.println(new Date(1511965277601L));
    }
}
