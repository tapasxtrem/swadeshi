/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import static com.getCategoryServlet.con;
import com.swadeshi.DBPool;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class NewServlet extends HttpServlet {

    static Connection con;
    static Statement stmt;
    ResultSet rs, rs2;
    OutputStream outStream;
    DataOutputStream out;
    String lastUpdate;

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            con = DBPool.get();
            stmt = con.createStatement();
            System.out.println("This is servlet call for MasterData list...");
            response.setContentType("text/html");
            String t1 = request.getParameter("date");
            String imei = request.getParameter("imei");
            long time = Long.parseLong(t1);//request.getParameter("date");
            JSONArray array = new JSONArray();
            JSONObject mainJsonObject = new JSONObject();

            PreparedStatement ps1 = con.prepareStatement("select * from swadeshi_CategoryMaster where lastUpdate>?");
            // ps1.setLong(1,1470748468);
            ps1.setLong(1, time);

            this.rs = ps1.executeQuery();
            JSONArray categoryJsonArray = new JSONArray();
            while (rs.next()) {
                //JSONObject obj = new JSONObject();        
                JSONObject categoryJsonObject = new JSONObject();
                categoryJsonObject.put("cid", this.rs.getInt("Category_ID"));
                categoryJsonObject.put("cname", this.rs.getString("Category_Name"));
                categoryJsonArray.add(categoryJsonObject);
                //array.add(categoryJsonObject);

            }
            // obj.put("END","***");

            // int i = array.lastIndexOf(o);
            PreparedStatement ps2 = con.prepareStatement("select Brand_ID,Company_Name,Chairman_Name,HO_Address,Ho_Number,WebSite_Name from swadeshi_BrandDetails where lastUpdate>?");
            ps2.setLong(1, time);
            this.rs = ps2.executeQuery();

            JSONArray brandDetJsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject brandDetRowJsonObject = new JSONObject();
                // ob.put("START", "###");
                brandDetRowJsonObject.put((Object) "Brand_ID", (Object) this.rs.getInt(1));
                brandDetRowJsonObject.put((Object) "Company_Name", (Object) this.rs.getString(2));
                brandDetRowJsonObject.put((Object) "Chairman_Name", (Object) this.rs.getString(3));
                brandDetRowJsonObject.put((Object) "HO_Address", (Object) this.rs.getString(4));
                brandDetRowJsonObject.put((Object) "Ho_Number", (Object) this.rs.getString(5));
                brandDetRowJsonObject.put((Object) "WebSite_Name", (Object) this.rs.getString(6));
                brandDetJsonArray.add(brandDetRowJsonObject);
            }
            //ob.put("END", "###");
            //array.add(ob);

            PreparedStatement ps3 = con.prepareStatement("select Brand_ID,Category_ID,Brand_Name,Brand_Type from swadeshi_BrandMaster  where lastUpdate>?");
            ps3.setLong(1, time);
            this.rs = ps3.executeQuery();

            JSONArray brandMasterJsonArray = new JSONArray();
            while (rs.next()) {
                //  o.put("START", "@");
                JSONObject brandMasterRowJsonObjct = new JSONObject();
                brandMasterRowJsonObjct.put((Object) "Brand_ID", (Object) this.rs.getInt(1));
                brandMasterRowJsonObjct.put((Object) "Category_ID", (Object) this.rs.getString(2));
                brandMasterRowJsonObjct.put((Object) "Brand_Name", (Object) this.rs.getString(3));
                brandMasterRowJsonObjct.put((Object) "Brand_Type", (Object) this.rs.getString(4));
                brandMasterJsonArray.add(brandMasterRowJsonObjct);
            }

            PreparedStatement ps4 = con.prepareStatement("select Brand_ID,lastUpdate from swadeshi_BrandDetails where lastUpdate>?");
            ps4.setLong(1, time);
            this.rs = ps4.executeQuery();

            JSONArray brandDetImgJsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject brandDetImgJsonObject = new JSONObject();
                // ob.put("START", "###");
                brandDetImgJsonObject.put((Object) "Brand_ID", (Object) this.rs.getInt(1));
                brandDetImgJsonObject.put((Object) "lastUpdate", (Object) this.rs.getString(2));

                brandDetImgJsonArray.add(brandDetImgJsonObject);
            }
            mainJsonObject.put("categories", categoryJsonArray);
            mainJsonObject.put("brandMaster", brandMasterJsonArray);
            mainJsonObject.put("brandDetails", brandDetJsonArray);
            mainJsonObject.put("brandImgDetails", brandDetImgJsonArray);
            // brandMasterJsonObjct.put("END", "@@@");
            // array.add(brandMasterJsonObjct);
            mainJsonObject.writeJSONString(response.getWriter());

        } catch (Exception ie) {
            DBPool.log(ie);
        }
    }

}
