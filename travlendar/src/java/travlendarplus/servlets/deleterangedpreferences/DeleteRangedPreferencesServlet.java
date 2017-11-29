/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.deleterangedpreferences;

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
import travlendarplus.response.responsedeleterangedpreferences.ResponseDeleteRangedPreferences;
import travlendarplus.response.responsedeleterangedpreferences.ResponseDeleteRangedPreferencesType;
import travlendarplus.user.User;
import travlendarplus.user.preferences.RangedPreference;
import travlendarplus.user.preferences.RangedPreferenceType;

/**
 *
 * @author Emilio
 */
@WebServlet(name = "DeleteRangedPreferencesServlet", urlPatterns = {"/deleterangedpreferences"})
public class DeleteRangedPreferencesServlet extends HttpServlet {

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
            int curr, i;
            //GETPARAMETERS
            String p1 = request.getParameter("user");
            String p2 = request.getParameter("pass");
            String[] prefs = request.getParameterValues("pref");
            User u = new User(p1, p2);
            RangedPreference[] ranprefs = new RangedPreference[prefs.length];
            //COMPUTE
            for(i = 0; i < prefs.length; i++){
                curr = Integer.parseInt(prefs[i]);
                ranprefs[i] = new RangedPreference(RangedPreferenceType.getForValue(curr), 0);
            }
            for(RangedPreference r : ranprefs)
                DataLayer.deletePreference(u, r);
            
            DataLayer.getPreferences(u);
            //RESPONSE
            ResponseDeleteRangedPreferences rr = new ResponseDeleteRangedPreferences(ResponseDeleteRangedPreferencesType.OK, u.getRangedPreferences());
            Gson gson = new GsonBuilder().create();
            gson.toJson(rr, out);
        } catch (IOException|SQLException e){
            request.getRequestDispatcher("/connectionerrordeleterangedpreferences").forward(request, response);
        } catch (InvalidInputException|UnconsistentValueException|NullPointerException|NumberFormatException e){
            request.getRequestDispatcher("/invalidinputdeleterangedpreferences").forward(request, response);
        } catch (InvalidLoginException e){
            request.getRequestDispatcher("/invalidlogindeleterangedpreferences").forward(request, response);
        }
    }
}
