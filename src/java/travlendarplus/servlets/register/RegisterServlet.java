/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.register;

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
import travlendarplus.exceptions.AlreadyExistingUserException;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.response.responseregister.ResponseRegister;
import travlendarplus.response.responseregister.ResponseRegisterType;
import travlendarplus.user.User;

/**
 *
 * @author matteo
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        computeResponse(request,response);
        
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
       computeResponse(request,response);
    }
    
    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            PrintWriter out=response.getWriter();
            //GET PARAMETERS
            String p1=request.getParameter("user");
            String p2=request.getParameter("pass");
            User u= new User(p1,p2);
            //COMPUTE
            u.registerInDB();
            //SEND RESPONSE
            ResponseRegister rr= new ResponseRegister(ResponseRegisterType.OK);
            Gson gson= new GsonBuilder().create();
            gson.toJson(rr,out);
        }catch(IOException|SQLException e){
            request.getRequestDispatcher("/connectionerrorregister").forward(request,response);
        } catch (InvalidInputException|NullPointerException ex) {
            request.getRequestDispatcher("/invalidinputregister").forward(request,response);
        } catch (AlreadyExistingUserException e){
            request.getRequestDispatcher("/registerusernameexists").forward(request,response);
        }
    }

}
