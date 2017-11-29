/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.getpreferences;

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
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.response.responsegetpreferences.ResponseGetPreferences;
import travlendarplus.response.responsegetpreferences.ResponseGetPreferencesType;
import travlendarplus.user.User;

/**
 *
 * @author Emilio
 */
@WebServlet(name = "GetPreferencesServlet", urlPatterns = {"/getpreferences"})
public class GetPreferencesServlet extends HttpServlet {
    
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
            User u = new User(p1, p2);
            //COMPUTE
            u.getPreferencesFromDB();
            //RESPONSE
            ResponseGetPreferences rr = new ResponseGetPreferences(ResponseGetPreferencesType.OK, u);
            Gson gson = new GsonBuilder().create();
            gson.toJson(rr, out);
        } catch (IOException|SQLException|UnconsistentValueException e){
            request.getRequestDispatcher("/connectionerrorgetpreferences").forward(request, response);
        } catch (InvalidInputException|NullPointerException e){
            request.getRequestDispatcher("/invalidinputgetpreferences").forward(request, response);
        } catch (InvalidLoginException e){
            request.getRequestDispatcher("/invalidlogingetpreferences").forward(request, response);
        }
    }
}