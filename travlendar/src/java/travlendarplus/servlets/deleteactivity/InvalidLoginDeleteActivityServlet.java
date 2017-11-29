package travlendarplus.servlets.deleteactivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import travlendarplus.response.responsedeleteactivity.ResponseDeleteActivity;
import travlendarplus.response.responsedeleteactivity.ResponseDeleteActivityType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Modified by mattiadifatta on 19/11/2017.
 */
@WebServlet(name="InvalidLoginDeleteActivityServlet", urlPatterns = {"/invalidlogindeleteactivity"} )
public class InvalidLoginDeleteActivityServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        computeResponse(request,response);

    }

    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = new PrintWriter(response.getWriter());
        ResponseDeleteActivity rda = new ResponseDeleteActivity(ResponseDeleteActivityType.DELETE_ACTIVITY_LOGIN_ERROR, null);
        Gson gson = new GsonBuilder().create();
        gson.toJson(rda,out);
    }
}
