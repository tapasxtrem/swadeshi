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
public class GetShortDetailsServlet extends HttpServlet {

    static Connection con;
    static Statement stmt;
    ResultSet rs;
    PrintWriter out;

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            con = DBPool.get();
            stmt = con.createStatement();
            System.out.println("This is servlet call for city name list...");
            this.out = response.getWriter();
            response.setContentType("text/html");
            int productId = Integer.parseInt(request.getParameter("tag").trim());
            boolean totalRow = false;
            String fullString = "";
            this.rs = stmt.executeQuery("select Brand_ID,Company_Name,Chairman_Name,Short_Address from swadeshi_BrandDetails  where Brand_ID = " + productId);
            JSONArray jarray = new JSONArray();
            if (this.rs.next()) {
                do {
                    JSONObject o = new JSONObject();
                    o.put((Object)"Brand_ID", (Object)this.rs.getInt(1));
                    o.put((Object)"Company_Name", (Object)this.rs.getString(2));
                    o.put((Object)"Chairman_Name", (Object)this.rs.getString(3));
                    o.put((Object)"Short_Address", (Object)this.rs.getString(4));
                    jarray.add((Object)o);
                } while (this.rs.next());
            }
            jarray.writeJSONString((Writer)this.out);
            con.close();
        }
        catch (Exception ie) {
            // empty catch block
            DBPool.log(ie);
        }
    }
}
