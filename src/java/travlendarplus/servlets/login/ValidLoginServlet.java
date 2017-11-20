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
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.InvalidPositionException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.response.responselogin.ResponseLogin;
import travlendarplus.response.responselogin.ResponseLoginType;
import travlendarplus.user.User;

/**
 *
 * @author matteo
 */
@WebServlet(name = "ValidLoginServlet", urlPatterns = {"/login"})
public class ValidLoginServlet extends HttpServlet{

    
    public ValidLoginServlet(){
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
        try{
            PrintWriter out=response.getWriter();
            //GET PARAMETERS
            String p1=request.getParameter("user");
            String p2=request.getParameter("pass");
            User u= new User(p1,p2);
            //COMPUTE
            if(!u.usernameExists())
                request.getRequestDispatcher("/notexistingusernamelogin").forward(request,response);
            if(!u.isValidLogin())
                request.getRequestDispatcher("/wrongpasswordlogin").forward(request,response);
            u.getCalendarFromDB();
            u.getPreferencesFromDB();
            u.getfavPositionsFromDB();
            //SEND RESPONSE
            ResponseLogin rl= new ResponseLogin(ResponseLoginType.OK,u);
            Gson gson= new GsonBuilder().create();
            gson.toJson(rl,out);
        }catch(IOException|SQLException|UnconsistentValueException|InvalidPositionException e){
            request.getRequestDispatcher("/connectionerrorlogin").forward(request,response);
        } catch (InvalidInputException|NullPointerException ex) {
            request.getRequestDispatcher("/invalidinputlogin").forward(request,response);
        } catch (InvalidLoginException ex) {
            //CHECKED BEFORE
        }
    }
}
