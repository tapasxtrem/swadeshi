/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swadeshi;

import com.swadeshi.DBPool;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author xtrem
 */
public class GetBrandDetailsServlet extends HttpServlet {

   
    static Connection con;
    static Statement stmt;
    ResultSet rs;
    PrintWriter out;

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            con = DBPool.get();
            stmt = con.createStatement();
            System.out.println("This is servlet call for name list...");
            this.out = response.getWriter();
            response.setContentType("text/html");
            int productId = Integer.parseInt(request.getParameter("tag").trim());
            String time=request.getParameter("time");

            boolean totalRow = false;
            String fullString = "";
            
             PreparedStatement ps = con.prepareStatement("select Brand_ID,Company_Name,Chairman_Name,HO_Address,Ho_Number,WebSite_Name from swadeshi_BrandDetails  where Brand_ID=? AND lastUpdate>?");
            //ps.setLong(1,1470492296);
            ps.setInt(1, productId);
            ps.setLong(2, Long.parseLong(time));
           this.rs=ps.executeQuery();
            //this.rs = stmt.executeQuery("select Brand_ID,Company_Name,Chairman_Name,HO_Address,Ho_Number,WebSite_Name from swadeshi_BrandDetails  where Brand_ID = " + productId +"AND  lastUpdate>?");
            JSONArray jarray = new JSONArray();
            while (this.rs.next()) {
                JSONObject o = new JSONObject();
                o.put((Object)"Brand_ID", (Object)this.rs.getInt(1));
                o.put((Object)"Company_Name", (Object)this.rs.getString(2));
                o.put((Object)"Chairman_Name", (Object)this.rs.getString(3));
                o.put((Object)"HO_Address", (Object)this.rs.getString(4));
                o.put((Object)"Ho_Number", (Object)this.rs.getString(5));
                o.put((Object)"WebSite_Name", (Object)this.rs.getString(6));
                jarray.add((Object)o);
            }
            jarray.writeJSONString((Writer)this.out);
            
            
            con.close();
        }
        catch (Exception ie) {
            DBPool.log(ie);
        }
    }
}