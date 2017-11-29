package travlendarplus.servlets.getcalendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import travlendarplus.response.responsegetcalendar.ResponseGetCalendar;
import travlendarplus.response.responsegetcalendar.ResponseGetCalendarType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Modified by mattiadifatta on 20/11/2017.
 */
@WebServlet(name="ConnectionProblemGetCalendarServlet", urlPatterns = {"/connectionerrorgetcalendar"})
public class ConnectionProblemGetCalendarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {

            PrintWriter out = new PrintWriter(response.getWriter());
            ResponseGetCalendar rgc = new ResponseGetCalendar(ResponseGetCalendarType.GET_CALENDAR_CONN_ERROR, null);
            Gson gson = new GsonBuilder().create();
            gson.toJson(rgc, out);
    }
}
