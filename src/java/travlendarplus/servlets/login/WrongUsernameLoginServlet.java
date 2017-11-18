/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import travlendarplus.response.responselogin.ResponseLogin;
import travlendarplus.response.responselogin.ResponseLoginType;

/**
 *
 * @author matteo
 */
public class WrongUsernameLoginServlet extends HttpServlet{
    public WrongUsernameLoginServlet(){
        super();
    }

    
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
        ResponseLogin rl= new ResponseLogin(ResponseLoginType.LOGIN_USERNAME_ERROR,null);
        Gson gson= new GsonBuilder().create();
        gson.toJson(rl,out);
    }
}
