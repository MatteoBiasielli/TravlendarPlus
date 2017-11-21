/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.updatebooleanpreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.response.responseupdatebooleanpreferences.ResponseUpdateBooleanPreferences;
import travlendarplus.response.responseupdatebooleanpreferences.ResponseUpdateBooleanPreferencesType;
import travlendarplus.user.User;
import travlendarplus.user.preferences.BooleanPreferencesSet;
import travlendarplus.user.preferences.Modality;

/**
 *
 * @author Emilio
 */
@WebServlet(name = "UpdateBooleanPreferencesServlet", urlPatterns = {"/updatebooleanpreferencesservlet"})
public class UpdateBooleanPreferencesServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        computeResponse(request, response);
    }
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        computeResponse(request, response);
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
    protected void computeResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            PrintWriter out = response.getWriter();
            //GETPARAMETERS
            String p1 = request.getParameter("user");
            String p2 = request.getParameter("pass");
            Boolean b1 = Boolean.parseBoolean(request.getParameter("pC"));
            Boolean b2 = Boolean.parseBoolean(request.getParameter("cS"));
            Boolean b3 = Boolean.parseBoolean(request.getParameter("pB"));
            Boolean b4 = Boolean.parseBoolean(request.getParameter("bS"));
            Boolean b5 = Boolean.parseBoolean(request.getParameter("pT"));
            Boolean b6 = Boolean.parseBoolean(request.getParameter("uT"));
            Integer b7 = Integer.parseInt(request.getParameter("m"));
            User u = new User(p1, p2);
            Modality m = Modality.getForValue(b7);
            BooleanPreferencesSet p = new BooleanPreferencesSet(b1, b2, b3, b4, b5, b6, m);
            //COMPUTE
            DataLayer.updatePreference(u, p);
            //RESPONSE
            ResponseUpdateBooleanPreferences rr = new ResponseUpdateBooleanPreferences(ResponseUpdateBooleanPreferencesType.OK);
            Gson gson = new GsonBuilder().create();
            gson.toJson(rr, out);
        } catch (IOException|SQLException e){
            request.getRequestDispatcher("/connectionerrorupdatebooleanpreferences").forward(request, response);
        } catch (InvalidInputException|UnconsistentValueException e){
            request.getRequestDispatcher("/invalidinputupdatebooleanpreferences").forward(request, response);
        } catch (InvalidLoginException e){
            request.getRequestDispatcher("/invalidloginupdatebooleanpreferences").forward(request, response);
        }
    }
}
