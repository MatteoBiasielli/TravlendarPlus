package travlendarplus.servlets.getcalendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.response.responsegetcalendar.ResponseGetCalendar;
import travlendarplus.response.responsegetcalendar.ResponseGetCalendarType;
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
 * Modified by mattiadifatta on 20/11/2017.
 */
@WebServlet(name="GetCalendarServlet", urlPatterns = {"/getcalendar"})
public class GetCalendarServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            //GET PARAMETERS
            PrintWriter out = new PrintWriter(response.getWriter());
            String usr = request.getParameter("user");
            String pss = request.getParameter("pass");

            User u = new User(usr, pss);
            //LOGIN CHECK
            if(!u.isValidLogin()) {
                request.getRequestDispatcher("/invalidlogingetcalendar").forward(request, response);
                return;
            }
            //UPDATE  CALENDAR IN USER
            u.getCalendarFromDB();
            //PREPARE AND SEND THE RESPONSE
            ResponseGetCalendar rgc = new ResponseGetCalendar(ResponseGetCalendarType.OK, u.getCalendar());
            Gson gson = new GsonBuilder().create();
            gson.toJson(rgc, out);
        }catch (SQLException | IOException | UnconsistentValueException e){
            request.getRequestDispatcher("/connectionerrorgetcalendar").forward(request, response);
        }catch (InvalidInputException|NullPointerException e){
            request.getRequestDispatcher("/invalidinputgetcalendar").forward(request, response);
        }catch (InvalidLoginException e){
            request.getRequestDispatcher("/invalidlogingetcalendar").forward(request, response);
        }

    }
}
