package travlendarplus.servlets.retrievenotificationsservlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.response.responseretrievenotifications.ResponseRetrieveNotifications;
import travlendarplus.response.responseretrievenotifications.ResponseRetrieveNotificationsType;
import travlendarplus.servlets.updateactivity.InvalidInputUpdateActivityServlet;
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
 * Modified by mattiadifatta on 23/11/2017.
 */
@WebServlet(name="RetrieveNotificationsServlet", urlPatterns = {"/retrievenotifications"})
public class RetrieveNotificationsServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            //GET PARAMETERS
            PrintWriter out = new PrintWriter(response.getWriter());
            String usr = request.getParameter("user");
            String pss = request.getParameter("pass");

            User u = new User(usr, pss);
            if(!u.isValidLogin())
                request.getRequestDispatcher("/invalidloginretrievenotifications").forward(request, response);
            //GET NOTIFICATIONS
            DataLayer.getNotification(u);
            //PREPARE AND SEND RESPONSE
            ResponseRetrieveNotifications rrn = new ResponseRetrieveNotifications(u.getNotifications(), ResponseRetrieveNotificationsType.OK);
            Gson gson = new GsonBuilder().create();
            gson.toJson(rrn, out);
        }catch(SQLException | IOException e){
            request.getRequestDispatcher("/connectionerrorretrievenotifications").forward(request, response);
        }catch(InvalidInputException | NullPointerException e){
            request.getRequestDispatcher("/invalidinputretrievenotifications").forward(request, response);
        }catch(InvalidLoginException e){
            request.getRequestDispatcher("/invalidloginretrievenotifications").forward(request, response);
        }
    }
}
