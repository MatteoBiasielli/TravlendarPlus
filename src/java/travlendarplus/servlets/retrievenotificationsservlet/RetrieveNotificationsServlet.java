package travlendarplus.servlets.retrievenotificationsservlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    private void computeResponse(HttpServletRequest request, HttpServletResponse response){

    }
}
