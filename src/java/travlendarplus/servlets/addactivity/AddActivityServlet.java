/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.addactivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import travlendarplus.calendar.activities.Activity;
import travlendarplus.calendar.activities.ActivityStatus;
import travlendarplus.calendar.activities.Break;
import travlendarplus.calendar.activities.FixedActivity;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.CannotBeAddedException;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.response.responseaddactivity.ResponseAddActivity;
import travlendarplus.response.responseaddactivity.ResponseAddActivityNotification;
import travlendarplus.response.responseaddactivity.ResponseAddActivityType;
import travlendarplus.user.User;

/**
 *
 * @author matteo
 */
@WebServlet(name = "AddActivityServlet", urlPatterns = {"/addactivity"})
public class AddActivityServlet extends HttpServlet{
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        computeResponse(request,response);
        
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
       computeResponse(request,response);
    }

    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            PrintWriter out=response.getWriter();
            //GET PARAMETERS
            String p1=request.getParameter("user");
            String p2=request.getParameter("pass");
            String label=request.getParameter("label");
            String notes=request.getParameter("notes");
            String locationText=request.getParameter("locationtext");
            String locationTag=request.getParameter("locationtag");
            String startText=request.getParameter("starttext");
            String startTag=request.getParameter("starttag");
            boolean flexible=Boolean.parseBoolean(request.getParameter("flexible"));
            long duration=Long.parseLong(request.getParameter("duration"));
            Date startDate=new Date(Long.parseLong(request.getParameter("start")));
            Date endDate=new Date(Long.parseLong(request.getParameter("end")));
            Activity actToAdd;
            if(locationText==null && locationTag==null || startText==null && startTag==null)
                request.getRequestDispatcher("/invalidinputaddactivity").forward(request, response);
            if(new Date().getTime()>startDate.getTime())
                request.getRequestDispatcher("/pastaddactivity").forward(request, response);
            User u=new User(p1,p2);
            u.getCalendarFromDB();
            if(flexible){
                actToAdd=new Break(startDate, endDate,label, notes,locationText, startText, ActivityStatus.NOT_STARTED, duration);
                u.getCalendar().addActivity((Break)actToAdd);
            }else{
                actToAdd=new FixedActivity(startDate, endDate,label, notes,locationText, startText, ActivityStatus.NOT_STARTED);
                actToAdd.calculateEstimatedTravelTime();
                u.getCalendar().addActivity((FixedActivity)actToAdd);
            }
            DataLayer.addActivity(u, actToAdd, locationTag, startTag);
            ResponseAddActivityNotification notif= actToAdd.generateRequiredNotification(u.getCalendar());
            u.getCalendarFromDB();
            ResponseAddActivity raa= new ResponseAddActivity(u,ResponseAddActivityType.OK,notif);
            Gson gson= new GsonBuilder().create();
            gson.toJson(raa,out);
        } catch (IOException|SQLException|UnconsistentValueException ex) {
            request.getRequestDispatcher("/connectionproblemaddactivity").forward(request, response);
        } catch (InvalidInputException|NullPointerException|NumberFormatException ex) {
            request.getRequestDispatcher("/invalidinputaddactivity").forward(request, response);
        } catch (InvalidLoginException ex) {
            request.getRequestDispatcher("/invalidloginaddactivity").forward(request, response);
        } catch (CannotBeAddedException ex) {
            request.getRequestDispatcher("/overlappingaddactivity").forward(request, response);
        }
    }
}
