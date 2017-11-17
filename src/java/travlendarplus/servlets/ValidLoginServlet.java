/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import travlendarplus.apimanager.APIManager;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.InvalidAddressException;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.user.User;

/**
 *
 * @author matteo
 */
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
    
    private void computeResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            PrintWriter out=response.getWriter();
            String p1=request.getParameter("user");
            String p2=request.getParameter("pass");
            User u= new User(p1,p2);
            Gson gson= new GsonBuilder().create();
            DataLayer.getCalendar(u);
            gson.toJson(u,out);
            //out.println(DataLayer.validLogin(p1, p2));
            
        }catch(IOException|InvalidInputException|SQLException|NullPointerException|InvalidLoginException|UnconsistentValueException e){
            PrintWriter out=response.getWriter();
            out.println(e.getStackTrace());
            request.getRequestDispatcher("/error").forward(request,response);
        }
        /*String json="{\"username\":\"cane\",\"password\":\"cane\",\"calendar\":{\"fixedActivities\":[{\"startDate\":\"Nov 10, 2017 12:23:54 AM\",\"endDate\":\"Nov 10, 2017 2:23:55 AM\",\"label\":\"aaa\",\"notes\":\"bbb\",\"locationAddress\":\"politecnico di milano\",\"startPlaceAddress\":\"via caduti di marcinelle 2 milano\",\"actStatus\":\"NOT_STARTED\",\"key\":2,\"keySet\":true},{\"startDate\":\"Nov 10, 2017 12:23:54 AM\",\"endDate\":\"Nov 10, 2017 2:23:55 AM\",\"label\":\"br\",\"notes\":\"br1\",\"locationAddress\":\"Lake Rd, Cambridge, NE 69022, USA\",\"startPlaceAddress\":\"Middle Canyon Rd, Tooele, UT 84074, USA\",\"actStatus\":\"NOT_STARTED\",\"key\":3,\"keySet\":true},{\"startDate\":\"Nov 10, 2017 12:23:54 AM\",\"endDate\":\"Nov 10, 2017 2:23:55 AM\",\"label\":\"br\",\"notes\":\"br1\",\"locationAddress\":\"Lake Rd, Cambridge, NE 69022, USA\",\"startPlaceAddress\":\"Middle Canyon Rd, Tooele, UT 84074, USA\",\"actStatus\":\"NOT_STARTED\",\"key\":4,\"keySet\":true},{\"startDate\":\"Nov 10, 2017 10:27:15 AM\",\"endDate\":\"Nov 10, 2017 10:27:25 AM\",\"label\":\"prova\",\"notes\":\"note di prova\",\"locationAddress\":\"Lake Rd, Cambridge, NE 69022, USA\",\"startPlaceAddress\":\"via caduti di marcinelle 2 milano\",\"actStatus\":\"ON_GOING\",\"key\":6,\"keySet\":true},{\"startDate\":\"Nov 10, 2017 10:27:15 AM\",\"endDate\":\"Nov 10, 2017 10:27:26 AM\",\"label\":\"prova\",\"notes\":\"note di prova\",\"locationAddress\":\"Lake Rd, Cambridge, NE 69022, USA\",\"startPlaceAddress\":\"via caduti di marcinelle 2 milano\",\"actStatus\":\"ON_GOING\",\"key\":7,\"keySet\":true},{\"startDate\":\"Nov 12, 2017 12:23:24 PM\",\"endDate\":\"Nov 12, 2017 12:23:34 PM\",\"label\":\"prova\",\"notes\":\"note di prova111\",\"locationAddress\":\"via stefanardo da vimercate 95 milano\",\"startPlaceAddress\":\"via caduti di marcinelle 2 milano\",\"actStatus\":\"ON_GOING\",\"key\":8,\"keySet\":true}],\"breaks\":[]}}";
        Gson gson= new GsonBuilder().create()
        User u= gson.fromJson(json,User.class);
        PrintWriter out=response.getWriter();
        out.println(u.getCalendar());
        out.println(u.getCalendar().getFixedActivities().get(0).getStartDate().getTime());
        out.println(new Date(1510269834000L));*/
    }
}
