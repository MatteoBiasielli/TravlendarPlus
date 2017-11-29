/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.travel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import travlendarplus.calendar.Calendar;
import travlendarplus.calendar.activities.FixedActivity;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.NoPathFoundException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.response.responsetravel.ResponseTravel;
import travlendarplus.response.responsetravel.ResponseTravelType;
import travlendarplus.travel.Route;
import travlendarplus.user.User;

/**
 *
 * @author matteo
 */
@WebServlet(name = "TravelServlet", urlPatterns = {"/travel"})
public class TravelServlet extends HttpServlet{
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        computeResponse(request,response);
        
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
       computeResponse(request,response);
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            PrintWriter out=response.getWriter();
            //GET PARAMETERS
            String p1=request.getParameter("user");
            String p2=request.getParameter("pass");
            User u= new User(p1,p2);
            //COMPUTE
            u.getCalendarFromDB();
            u.getPreferencesFromDB();
            FixedActivity nextAct=computeNextActivity(u.getCalendar());
            if(nextAct==null)
                request.getRequestDispatcher("/noactivitytravel").forward(request,response);
            ArrayList<Route> routes= nextAct.computeTravels(u);
            //SEND RESPONSE
            ResponseTravel rt= new ResponseTravel(routes, ResponseTravelType.OK);
            Gson gson= new GsonBuilder().create();
            gson.toJson(rt,out);
        }catch(IOException|SQLException|UnconsistentValueException e){
            request.getRequestDispatcher("/connectionerrortravel").forward(request,response);
        } catch (InvalidInputException|NullPointerException ex) {
            request.getRequestDispatcher("/invalidinputtravel").forward(request,response);
        } catch (InvalidLoginException ex) {
            request.getRequestDispatcher("/invalidlogintravel").forward(request,response);
        } catch(NoPathFoundException e){
            request.getRequestDispatcher("/noroutetravel").forward(request,response);
        }
    }
    
    /**
     * Given a calendar, finds the next activity.
     * @param c is the calendar.
     * @return the next activity from now.
     */
    private FixedActivity computeNextActivity(Calendar c){
        Date now= new Date();
        FixedActivity ris=null;
        ArrayList<FixedActivity> fa=c.getFixedActivities();
        for(FixedActivity act:fa)
            if(act.getStartDate().after(now) && (ris==null || ris.isAfter(act)))
                ris=act;
        return ris;
    }
}
