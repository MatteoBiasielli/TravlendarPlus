package travlendarplus.servlets.updateactivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import travlendarplus.response.responseupdateactivity.ResponseUpdateActivity;
import travlendarplus.response.responseupdateactivity.ResponseUpdateActivityType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.UnresolvedAddressException;

/**
 * Modified by mattiadifatta on 21/11/2017.
 */
@WebServlet(name="ConnectionProblemUpdateActivityServlet", urlPatterns = {"/connectionerrorupdateactivity"})
public class ConnectionProblemUpdateActivityServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request, response);
    }

    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
            PrintWriter out = new PrintWriter(response.getWriter());
            ResponseUpdateActivity rua = new ResponseUpdateActivity(null, ResponseUpdateActivityType.UPDATE_ACTIVITY_CONN_ERROR, null);
            Gson gson = new GsonBuilder().create();
            gson.toJson(rua, out);
    }
}
