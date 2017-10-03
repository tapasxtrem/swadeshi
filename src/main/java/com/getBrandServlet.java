package com;

import com.swadeshi.DBPool;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class getBrandServlet
extends HttpServlet {
    static Connection con;
    static Statement stmt;
    ResultSet rs;
    OutputStream outStream;
    DataOutputStream out;

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            con = DBPool.get();
            stmt = con.createStatement();
            System.out.println("This is servlet call for city name list...");
            this.outStream = response.getOutputStream();
            this.out = new DataOutputStream(this.outStream);
            response.setContentType("text/html");
            String branchName = "";
            String branchId = "";
           // String categoryID = request.getParameter("categoryID");
            int totalRow = 0;
            String fullString = "";
            Vector<String> vecDeshi = new Vector<String>();
            Vector<String> vecViDeshi = new Vector<String>();
            this.rs = stmt.executeQuery("select Brand_ID,Brand_Name,Brand_Type from swadeshi_BrandMaster where Category_ID = " + 3 + " order by Brand_ID ASC");
            while (this.rs.next()) {
                String id = this.rs.getString(1);
                String name = this.rs.getString(2);
                String type = this.rs.getString(3);
                if (type.equalsIgnoreCase("0")) {
                    name = "$" + name;
                    String srtData = id + "%" + name + "#";
                    vecDeshi.add(srtData);
                } else if (type.equalsIgnoreCase("1")) {
                    name = "@" + name;
                    String srtData = id + "%" + name + "#";
                    vecViDeshi.add(srtData);
                }
                int maxLength = vecDeshi.size();
                if (maxLength < vecViDeshi.size()) {
                    maxLength = vecViDeshi.size();
                }
                for (int i = 0; i < maxLength; ++i) {
                    if (i < vecDeshi.size()) {
                        fullString = fullString + ((String)vecDeshi.get(i)).toString().trim() + "#";
                    }
                    if (i >= vecViDeshi.size()) continue;
                    fullString = fullString + ((String)vecViDeshi.get(i)).toString().trim() + "#";
                }
                System.out.println("Branch id  " + id);
                System.out.println("Branch List  " + name);
                ++totalRow;
            }
            System.out.println("final String Data : " + fullString);
            System.out.println("Total Rows " + totalRow);
            this.out.writeUTF(fullString);
            con.close();
        }
        catch (SQLException ie) {
            DBPool.log(ie);
        }
        catch (Exception ex) {
             DBPool.log(ex);
        }
    }
}