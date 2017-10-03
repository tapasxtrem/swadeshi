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

/**
 *
 * @author xtrem
 */
public class getCategoryServlet extends HttpServlet {

    static Connection con;
    static Statement stmt;
    ResultSet rs,rs2;
    OutputStream outStream;
    DataOutputStream out;
    String lastUpdate;

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            con = DBPool.get();
            stmt = con.createStatement();
            System.out.println("This is servlet call for category list...");
            response.setContentType("text/html");
            String time=request.getParameter("tag");
            PreparedStatement ps1 = con.prepareStatement("select * from swadeshi_CategoryMaster where lastUpdate>?");
           // ps1.setLong(1,1470748468);
            ps1.setLong(1, Long.parseLong(time));
            this.rs=ps1.executeQuery();
            JSONArray array = new JSONArray();
                    while(rs.next()) {
                        JSONObject obj = new JSONObject();
                        obj.put("cid",this.rs.getInt("Category_ID"));
                        obj.put("cname", this.rs.getString("Category_Name"));
                        array.add(obj);
                      }
                    array.writeJSONString(response.getWriter());
                    
                    
              PreparedStatement ps2 = con.prepareStatement("select Brand_ID,Company_Name,Chairman_Name,HO_Address,Ho_Number,WebSite_Name from swadeshi_BrandDetails lastUpdate>?");
              ps2.setLong(1, Long.parseLong(time));            
             // ps2.setLong(1,1470492296);
              this.rs=ps2.executeQuery();
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
            jarray.writeJSONString(response.getWriter());
            
            PreparedStatement ps3 = con.prepareStatement("select * from swadeshi_BrandMaster  where lastUpdate>?");
            //ps3.setLong(1,1470492296);
            ps3.setLong(1, Long.parseLong(time));
            this.rs=ps2.executeQuery();
            JSONArray jsarray = new JSONArray();
            while (this.rs.next()) {
                JSONObject o = new JSONObject();
                o.put((Object)"Brand_ID", (Object)this.rs.getInt(1));
                o.put((Object)"Category_ID", (Object)this.rs.getString(2));
                o.put((Object)"Brand_Name", (Object)this.rs.getString(3));
                o.put((Object)"Brand_Type", (Object)this.rs.getString(4));
                jsarray.add((Object)o);
            }
            jsarray.writeJSONString(response.getWriter());
                    con.close();
                    }
          catch (Exception ie) {
            DBPool.log(ie);
        }
    }
}        
            
            
            /*
            
            
            
            
            
                 parseJsonArray(jsonArra1,1);		
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


parseJsonArray(JsonArray jsonArr,int tag){
	
	for(int i=0;i<jsonArr.length;i++){
		
		if(tag == 1){
			InsertUpdateCategoryDB(jsonArr.getJsonObject(i).getString("name"),jsonArr.getJsonObject(i).getString("id"))
		
		}		
		
	}
	
}
            
            
            
            
            
           /* 
            this.outStream = response.getOutputStream();
            this.out = new DataOutputStream(this.outStream);
            response.setContentType("text/html");
            String categoryName = "";
            String categoryId = "";
            int totalRow = 0;
          
           
            this.rs = stmt.executeQuery("select * from swadeshi_CategoryMaster");

            String fullString = "";
            while (this.rs.next()) {
                String id = this.rs.getString(1);
                String name = this.rs.getString(2);
                long date=this.rs.getLong(3);
               // Timestamp ts=rs.getTimestamp(3);
                //Date date=new java.util.Date(ts.getTime());
                fullString = fullString + id + "@" + name + "@"+ date + "#";
                System.out.println("Category id:  " + id);
                System.out.println("Category Name:  " + name);
                System.out.println("lastUpdate: "+ date);
                ++totalRow;
            }
            
            System.out.println("final String: " + fullString);
            System.out.println("Total Rows " + totalRow);
            out.writeUTF(fullString.trim());
            
            this.rs2 = stmt.executeQuery("select max(lastUpdate) from swadeshi_CategoryMaster");
            if(this.rs2.next()){
            long MaxDate=this.rs2.getLong(1);
            System.out.println("Maxdate: "+ MaxDate);
            }
            con.close();
        }
        catch (SQLException ie) {
            System.out.println("Error : " + ie.toString());
        }
        catch (Exception ex) {
            System.out.println("Error : " + ex.toString());
        }
    }*/
