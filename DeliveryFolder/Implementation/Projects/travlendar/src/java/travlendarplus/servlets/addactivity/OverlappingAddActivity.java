/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.addactivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import travlendarplus.response.responseaddactivity.ResponseAddActivity;
import travlendarplus.response.responseaddactivity.ResponseAddActivityType;

/**
 *
 * @author matteo
 */
@WebServlet(name = "OverlappingAddActivity", urlPatterns = {"/overlappingaddactivity"})
public class OverlappingAddActivity extends HttpServlet{
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        computeResponse(request,response);
        
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
       computeResponse(request,response);
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
    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        PrintWriter out= new PrintWriter(response.getWriter());
        ResponseAddActivity raa= new ResponseAddActivity(null,ResponseAddActivityType.ADD_ACTIVITY_OVERLAPPING,null);
        Gson gson= new GsonBuilder().create();
        gson.toJson(raa,out);
    }
    
}
