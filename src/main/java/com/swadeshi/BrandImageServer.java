/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swadeshi;

import com.swadeshi.DBPool;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author xtrem
 */
public class BrandImageServer extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/jpg");
        try {
            String tag = request.getParameter("tag");
            if (tag == null) {
                return;
            }
            int tag2 = Integer.parseInt(tag);
            Connection con = DBPool.get();
            PreparedStatement ps = con.prepareStatement("select Company_Logo from swadeshi_BrandDetails where Brand_ID= ?");
            ps.setInt(1, tag2);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BufferedImage image = ImageIO.read(rs.getBinaryStream(1));
                image = this.mainpulate(image, request);
                ImageIO.write((RenderedImage)image, "jpeg", (OutputStream)response.getOutputStream());
            }
            try {
                con.close();
            }
            catch (SQLException ex) {
            DBPool.log(ex);
            }
        }
        catch (SQLException ex) {
            DBPool.log(ex);
            // empty catch block
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }

    private BufferedImage mainpulate(BufferedImage image, HttpServletRequest request) {
        if (request.getParameter("w") != null) {
            int w = Integer.parseInt(request.getParameter("w"));
            int hei = image.getHeight();
            int wid = image.getWidth();
            float ratio = (float)((double)hei / ((double)wid * 1.0));
            int h = (int)((float)w * ratio);
            if (request.getParameter("h") != null) {
                h = Integer.parseInt(request.getParameter("h"));
            }
            BufferedImage newImage = new BufferedImage(w, h, 1);
            Graphics2D g = newImage.createGraphics();
            g.drawImage(image, 0, 0, w, h, null);
            g.dispose();
            return newImage;
        }
        return image;
    }
}
