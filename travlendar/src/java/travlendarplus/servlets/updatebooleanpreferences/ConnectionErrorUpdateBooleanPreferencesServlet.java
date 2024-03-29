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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import travlendarplus.response.responseupdatebooleanpreferences.ResponseUpdateBooleanPreferences;
import travlendarplus.response.responseupdatebooleanpreferences.ResponseUpdateBooleanPreferencesType;

/**
 *
 * @author Emilio
 */
@WebServlet(name = "ConnectionErrorUpdateBooleanPreferencesServlet", urlPatterns = {"/connectionerrorupdatebooleanpreferences"})
public class ConnectionErrorUpdateBooleanPreferencesServlet extends HttpServlet {

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
        PrintWriter out= new PrintWriter(response.getWriter());
        ResponseUpdateBooleanPreferences rr= new ResponseUpdateBooleanPreferences(ResponseUpdateBooleanPreferencesType.UPDATE_BOOLEAN_PREFERENCES_CONNECTION_ERROR);
        Gson gson= new GsonBuilder().create();
        gson.toJson(rr,out);
    }
}
