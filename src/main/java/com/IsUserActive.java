/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import com.swadeshi.DBPool;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

public class IsUserActive
  extends HttpServlet
{
  public IsUserActive() {}
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    CallableStatement cst = null;
    Connection con = null;
    ResultSet rs = null;
    boolean isGroupMember = false;
    try {
      String mobNo = request.getParameter("mobNo");
      con = DBPool.get();
      cst = con.prepareCall("{call Proc_IsUserActive(?)}");
      cst.setString(1, mobNo);
      rs = cst.executeQuery();
      if (rs.next()) {
        isGroupMember = rs.getBoolean(1);
      }
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("isUserActive", Boolean.valueOf(isGroupMember));
      jsonObject.writeJSONString(response.getWriter());
    }
    catch (Exception e) {
      e.printStackTrace();
      DBPool.log(e);
    } finally {
      DBPool.close(con, cst, rs);
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
