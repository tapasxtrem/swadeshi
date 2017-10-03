/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swadeshi;
import com.swadeshi.DBPool;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;




/**
 *
 * @author xtrem
 */
public class BrandDetails_UI extends HttpServlet {

     /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int brandId = Integer.parseInt(request.getParameter("brandId").trim());
        String companyName = request.getParameter("companyName");
        String chairmanName = request.getParameter("chairmanName");
        String hoAddress = request.getParameter("hoAddress");
        String shAddress = request.getParameter("shAddress");
        String hoNumber = request.getParameter("hoNumber").trim();
        String websiteName = request.getParameter("websiteName");
        InputStream inputStream = null;
        Part filePart = request.getPart("logo");
        if (filePart != null) {
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());
            inputStream = filePart.getInputStream();
        }
        Connection conn = null;
        String message = null;
        try {
            int row;
            conn = DBPool.get();
            String sql = "INSERT INTO swadeshi_BrandDetails (Brand_ID,Company_Name, Chairman_Name,HO_Address,Short_Address,Ho_Number,WebSite_Name, Company_Logo) values (?,?,?, ?, ?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, brandId);
            statement.setString(2, companyName);
            statement.setString(3, chairmanName);
            statement.setString(4, hoAddress);
            statement.setString(5, shAddress);
            statement.setString(6, hoNumber);
            statement.setString(7, websiteName);
            if (inputStream != null) {
                statement.setBlob(8, inputStream);
            }
            if ((row = statement.executeUpdate()) > 0) {
                message = "File uploaded and saved into database";
            }
        }
        catch (SQLException ex) {
            message = "ERROR: " + ex.getMessage();
            ex.printStackTrace();
            DBPool.log(ex);
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                     DBPool.log(ex);
                }
            }
            request.setAttribute("Message", (Object)message);
            this.getServletContext().getRequestDispatcher("/swadeshi/Message.jsp").forward((ServletRequest)request, (ServletResponse)response);
        }
    }
}