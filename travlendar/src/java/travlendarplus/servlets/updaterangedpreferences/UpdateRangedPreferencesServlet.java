/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.updaterangedpreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.response.responseupdaterangedpreferences.ResponseUpdateRangedPreferences;
import travlendarplus.response.responseupdaterangedpreferences.ResponseUpdateRangedPreferencesType;
import travlendarplus.user.User;
import travlendarplus.user.preferences.RangedPreference;
import travlendarplus.user.preferences.RangedPreferenceType;

/**
 *
 * @author Emilio
 */
@WebServlet(name = "UpdateRangedPreferencesServlet", urlPatterns = {"/updaterangedpreferences"})
public class UpdateRangedPreferencesServlet extends HttpServlet {

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
            int currp, currv, i;
            //GETPARAMETERS
            String p1 = request.getParameter("user");
            String p2 = request.getParameter("pass");
            String[] prefs = request.getParameterValues("pref");
            String[] values = request.getParameterValues("val");
            User u = new User(p1, p2);
            RangedPreference[] ranprefs = new RangedPreference[prefs.length];
            ArrayList<RangedPreference> toSend;
            //COMPUTE
            for(i = 0; i < prefs.length; i++){
                currp = Integer.parseInt(prefs[i]);
                currv = Integer.parseInt(values[i]);
                ranprefs[i] = new RangedPreference(RangedPreferenceType.getForValue(currp), currv);
            }
            for(RangedPreference r : ranprefs)
                DataLayer.updatePreference(u, r);
            
            DataLayer.getPreferences(u);
            toSend = u.getRangedPreferences();
            //RESPONSE
            ResponseUpdateRangedPreferences rr = new ResponseUpdateRangedPreferences(ResponseUpdateRangedPreferencesType.OK, toSend);
            Gson gson = new GsonBuilder().create();
            gson.toJson(rr, out);
        } catch (IOException|SQLException e){
            request.getRequestDispatcher("/connectionerrorupdaterangedpreferences").forward(request, response);
        } catch (InvalidInputException|UnconsistentValueException|NullPointerException|NumberFormatException e){
            request.getRequestDispatcher("/invalidinputupdaterangedpreferences").forward(request, response);
        } catch (InvalidLoginException e){
            request.getRequestDispatcher("/invalidloginupdaterangedpreferences").forward(request, response);
        }
    }
}
