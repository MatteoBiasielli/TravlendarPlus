/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.deletetag;

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
import travlendarplus.exceptions.InvalidPositionException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.response.responsedeletetag.ResponseDeleteTag;
import travlendarplus.response.responsedeletetag.ResponseDeleteTagType;
import travlendarplus.user.User;

/**
 *
 * @author matteo
 */
@WebServlet(name = "DeleteTagServlet", urlPatterns = {"/deletetag"})
public class DeleteTagServlet extends HttpServlet{
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
            String tag=request.getParameter("tag");
            User u= new User(p1,p2);
            //COMPUTE
            if(!u.isValidLogin())
                request.getRequestDispatcher("/invalidlogindeletetag").forward(request,response);
            DataLayer.deleteFavPosition(u, tag);
            u.getfavPositionsFromDB();
            //SEND RESPONSE
            ResponseDeleteTag rdt= new ResponseDeleteTag(ResponseDeleteTagType.OK,u.getFavPositions());
            Gson gson= new GsonBuilder().create();
            gson.toJson(rdt,out);
        }catch(IOException|SQLException|InvalidPositionException|UnconsistentValueException e){
            request.getRequestDispatcher("/connectionerrordeletetag").forward(request,response);
        } catch (InvalidInputException|NullPointerException ex) {
            request.getRequestDispatcher("/invalidinputdeletetag").forward(request,response);
        } catch (InvalidLoginException ex) {
            //CHECKED BEFORE
        }
    }
}
