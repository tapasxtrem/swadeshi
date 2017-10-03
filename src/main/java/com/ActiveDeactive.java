/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import com.swadeshi.DBPool;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActiveDeactive
  extends HttpServlet
{
  public ActiveDeactive() {}
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    Connection con = null;
    CallableStatement cls = null;
    try {
      PrintWriter out = response.getWriter();
      String mobNo = request.getParameter("id");
      boolean active = Boolean.parseBoolean(request.getParameter("active").trim());
      con = DBPool.get();
      cls = con.prepareCall("{call Proc_ActiveDeactiveChatUser(?,?)}");
      cls.setString(1, mobNo);
      cls.setBoolean(2, active);
      cls.execute();
      response.sendRedirect("/swadeshi/chatuser.jsp");
    } catch (Exception ex) {
      DBPool.log(ex);
    } finally {
      DBPool.close(con, cls);
    }
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    processRequest(request, response);
  }
 
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    processRequest(request, response);
  }
  
  public String getServletInfo()
  {
    return "Short description";
  }
}
