package travlendarplus.servlets.updateactivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import travlendarplus.calendar.activities.Activity;
import travlendarplus.calendar.activities.ActivityStatus;
import travlendarplus.calendar.activities.Break;
import travlendarplus.calendar.activities.FixedActivity;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.*;
import travlendarplus.response.responseaddactivity.ResponseAddActivityNotification;
import travlendarplus.response.responseupdateactivity.ResponseUpdateActivity;
import travlendarplus.response.responseupdateactivity.ResponseUpdateActivityType;
import travlendarplus.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;

/**
 * Modified by mattiadifatta on 21/11/2017.
 */
@WebServlet(name="UpdateActivityServlet", urlPatterns = {"/updateactivity"})
public class UpdateActivityServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            int travelTime;
            //GET PARAMETERS
            PrintWriter out = new PrintWriter(response.getWriter());
            String usr = request.getParameter("user");
            String pss = request.getParameter("pass");
            int actId = Integer.parseInt(request.getParameter("activityid"));
            String label = request.getParameter("label");
            String notes = request.getParameter("notes");
            String locText = request.getParameter("locationtext");
            String locTag = request.getParameter("locationtag");
            String startText = request.getParameter("starttext");
            String startTag = request.getParameter("starttag");
            boolean flexible = Boolean.parseBoolean(request.getParameter("flexible"));
            long duration = Long.parseLong(request.getParameter("duration"));
            Date startDate = new Date(Long.parseLong(request.getParameter("start")));
            Date endDate = new Date(Long.parseLong(request.getParameter("end")));
            //CONTROLS
            if(locText==null && locTag==null || startText==null && startTag==null
                    || endDate.before(startDate) || endDate.equals(startDate)) {
                request.getRequestDispatcher("/invalidinputupdateactivity").forward(request, response);
                return;
            }

            if(new Date().getTime() > startDate.getTime()){
                request.getRequestDispatcher("/pastupdateactivity").forward(request, response);
                return;
            }
            //UPDATE ACTIVITY IN USER
            User u = new User(usr, pss);
            u.getCalendarFromDB();
            Activity newActivity;

            if(flexible){
                newActivity = new Break(startDate, endDate, label, notes, locText, startText, ActivityStatus.NOT_STARTED, duration);
                newActivity.setKey(actId);
                u.getCalendar().deleteActivity((Break)newActivity);
                travelTime = 1;
                u.getCalendar().addActivity((Break)newActivity);
            }else{
                newActivity = new FixedActivity(startDate, endDate, label, notes, locText, startText, ActivityStatus.NOT_STARTED);
                newActivity.setKey(actId);
                u.getCalendar().deleteActivity((FixedActivity)newActivity);
                travelTime = newActivity.calculateEstimatedTravelTime(startTag,locTag,u);
                u.getCalendar().addActivity((FixedActivity)newActivity);
            }
            //UPDATE ACTIVITY IN DB
            DataLayer.updateActivity(u, newActivity, locTag, startTag);
            ResponseAddActivityNotification not = newActivity.generateRequiredNotification(u.getCalendar());
            u.getCalendarFromDB();
            //PREPARE AND SEND RESPONSE
            ResponseUpdateActivityType type;
            if(travelTime == 0)
                type = ResponseUpdateActivityType.OK_ESTIMATED_TIME;
            else
                type = ResponseUpdateActivityType.OK;

            ResponseUpdateActivity rua = new ResponseUpdateActivity(u, type, not);
            Gson gson = new GsonBuilder().create();
            gson.toJson(rua, out);

        }catch(IOException | SQLException | UnconsistentValueException e){
            request.getRequestDispatcher("/connectionerrorupdateactivity").forward(request, response);
        }catch(InvalidInputException | NoSuchActivityException | NumberFormatException | NullPointerException | InvalidPositionException e){
            request.getRequestDispatcher("/invalidinputupdateactivity").forward(request, response);
        }catch(CannotBeAddedException e){
            request.getRequestDispatcher("/overlappingupdateactivity").forward(request, response);
        }catch(InvalidLoginException e){
            request.getRequestDispatcher("/invalidloginupdateactivity").forward(request, response);
        }
    }
}
