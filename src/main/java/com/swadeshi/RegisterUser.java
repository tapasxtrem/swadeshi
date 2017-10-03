/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swadeshi;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author xtrem
 */
public class RegisterUser extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs    
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        //changed try with resources to only try
        try  {
            GlobalClass gclass = new GlobalClass();
            String requestString = gclass.getStringFromRequest(request);
            DBPool.log("Swadeshi MessageConfirmatin Req param :: \n" + requestString);
            //JSONObject jsonReq = new JSONObject(requestString);
            
            PrintWriter out = response.getWriter();
           JSONObject result  = new JSONObject();
           
           String imei = request.getHeader("IMEI");
           
           if(imei==null)
               imei="";
           String mobileos = request.getParameter("os");
           //String clientcode = request.getParameter("clientcode");
          // String mobileno = request.getParameter("mobileno");
           String regid = request.getParameter("regid");
          // if(mobileno!=null){
           if(regid!=null){
           if(GlobalClass.insertUpdateUser(regid, imei, mobileos))          
           {
              result.put("result", 0);//update call
              
           }
           else{
               result.put("result", 1);
            //   result.put("sms",sendSMS(mobileno));
           }              
           }
           
           
           
          //}//else{
            //   out.println("URL PARAMETER ERROR !!!");
         //  }
           
           result.writeJSONString(out);
           
        }
        catch(Exception e)
        {
            DBPool.log(e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

  

}
