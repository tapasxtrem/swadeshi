/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import com.swadeshi.DBPool;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserData
  extends HttpServlet
{
  static Connection con;
  static Statement stmt;
  ResultSet rs;
  PreparedStatement ps;
  PreparedStatement psU;
  OutputStream outStream;
  DataOutputStream out;
  String UserName;
  String Email;
  String Pass;
  String Mobile;
  String Addr1;
  String Addr2;
  String Location;
  String State;
  String PostalCode;
  
  public UserData() {}
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    try
    {
      con = DBPool.get();
      stmt = con.createStatement();
      System.out.println("This is servlet call for userData list...");
      response.setContentType("text/html");
      

      String str = request.getParameter("data");
      
      String[] tempArray = str.split("#");
      if (tempArray != null)
      {
        UserName = tempArray[0];
        Email = tempArray[1];
        Pass = tempArray[2];
        Mobile = tempArray[3];
        Addr1 = tempArray[4];
        Addr2 = tempArray[5];
        Location = tempArray[6];
        State = tempArray[7];
        PostalCode = tempArray[8];
      }
      
      CallableStatement cs = null;
      cs = con.prepareCall("{call proc_UserRegisteration(?,?,?,?,?,?,?,?,?)}");
      
      cs.setString(1, UserName);
      cs.setString(2, Email);
      cs.setString(3, Pass);
      cs.setString(4, Mobile);
      cs.setString(5, Addr1);
      cs.setString(6, Addr2);
      cs.setString(7, Location);
      cs.setString(8, State);
      cs.setString(9, PostalCode);
      
        boolean resultSet = cs.execute();
            if(resultSet) {
                DBPool.log("Registration Details");
               DBPool.log(UserName+","+Email+""+Pass+""+Mobile+""+Addr1+""+Addr2+""+Location+""+State+""+PostalCode);
               
            }
            else{
                
            }
      
      con.close();
    } catch (Exception ex) {
      DBPool.log(ex);
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
