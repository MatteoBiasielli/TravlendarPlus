package travlendarplus.servlets.retrievenotificationsservlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import travlendarplus.response.responseretrievenotifications.ResponseRetrieveNotifications;
import travlendarplus.response.responseretrievenotifications.ResponseRetrieveNotificationsType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Modified by mattiadifatta on 25/11/2017.
 */
@WebServlet(name = "InvalidInputRetrieveNotifications", urlPatterns = {"/invalidinputretrievenotifications"})
public class InvalidInputRetrieveNotifications extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out  = new PrintWriter(response.getWriter());
        ResponseRetrieveNotifications rrn = new ResponseRetrieveNotifications(null, ResponseRetrieveNotificationsType.NOTIFICATIONS_WRONG_INPUT);
        Gson gson = new GsonBuilder().create();
        gson.toJson(rrn, out);
    }
}
