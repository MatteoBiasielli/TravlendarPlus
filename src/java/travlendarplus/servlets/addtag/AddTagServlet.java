/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets.addtag;

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
import travlendarplus.apimanager.APIManager;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.AlreadyExistingTagException;
import travlendarplus.exceptions.InvalidAddressException;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.InvalidPositionException;
import travlendarplus.response.responseaddtag.ResponseAddTag;
import travlendarplus.response.responseaddtag.ResponseAddTagType;
import travlendarplus.travel.Position;
import travlendarplus.user.User;

/**
 *
 * @author matteo
 */
@WebServlet(name = "AddTagServlet", urlPatterns = {"/addtag"})
public class AddTagServlet extends HttpServlet{
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
            String addr=request.getParameter("addr");
            User u= new User(p1,p2);
            //COMPUTE
            Position pos= APIManager.googleGeocodingRequest(addr);
            DataLayer.addFavPosition(u, pos,tag);
            u.getfavPositionsFromDB();
            //SEND RESPONSE
            ResponseAddTag rat= new ResponseAddTag(ResponseAddTagType.OK,u.getFavPositions());
            Gson gson= new GsonBuilder().create();
            gson.toJson(rat,out);
        }catch(IOException|SQLException|InvalidPositionException e){
            request.getRequestDispatcher("/connectionerroraddtag").forward(request,response);
        } catch (InvalidInputException|NullPointerException|InvalidAddressException ex) {
            request.getRequestDispatcher("/invalidinputaddtag").forward(request,response);
        } catch (InvalidLoginException ex) {
            request.getRequestDispatcher("/invalidloginaddtag").forward(request,response);
        } catch (AlreadyExistingTagException ex) {
            request.getRequestDispatcher("/tagexistsaddtag").forward(request,response);
        }
    }
}
