package travlendarplus.servlets.deleteactivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import travlendarplus.calendar.activities.Activity;
import travlendarplus.calendar.activities.FixedActivity;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.response.responsedeleteactivity.ResponseDeleteActivity;
import travlendarplus.response.responsedeleteactivity.ResponseDeleteActivityType;
import travlendarplus.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Modified by mattiadifatta on 19/11/2017.
 */
@WebServlet(name= "DeleteActivityServlet", urlPatterns = {"/deleteactivity"} )
public class DeleteActivityServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request,response);
    }

    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            PrintWriter out = response.getWriter();
            //GET PARAMETERS FROM REQUEST
            String usr = request.getParameter("user");
            String pss = request.getParameter("pass");
            Activity act = new FixedActivity();
            act.setKey(Integer.parseInt(request.getParameter("activityid")));

            User u = new User(usr, pss);
            //LOGIN CHECK
            if(!u.isValidLogin())
                request.getRequestDispatcher("/invalidlogindeleteactivity").forward(request, response);
            //DELETE ACTIVITY
            DataLayer.deleteActivity(u, act);
            //UPDATE USER
            u.getCalendarFromDB();
            //PREPARE AND SEND RESPONSE
            ResponseDeleteActivity rda = new ResponseDeleteActivity(ResponseDeleteActivityType.OK, u.getCalendar());
            Gson gson = new GsonBuilder().create();
            gson.toJson(rda, out);

        }catch (IOException | SQLException | UnconsistentValueException e){
            request.getRequestDispatcher("/connectionerrordeleteactivity").forward(request, response);
        }catch (InvalidInputException | NumberFormatException | NullPointerException e){
            request.getRequestDispatcher("/invalidinputdeleteactivity").forward(request, response);
        }catch (InvalidLoginException e) {
            request.getRequestDispatcher("/invalidlogindeleteactivity").forward(request, response);
        }
    }
}
