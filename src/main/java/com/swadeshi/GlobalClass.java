/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swadeshi;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.swadeshi.DBPool;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;

/**
 *
 * @author xtrem
 */
public class GlobalClass {
    
    public static String android = "ANDROID";
  //  public static String ios = "IOS";
   // public static InputStream APPLE_KEYSTORE_DIR_PATH = null;//"/PushChat.p12";
    private final static String GOOGLE_SERVER_KEY = "AIzaSyDhGqVzVYk1t-NxQl5PNPRrnUjawediP9c";//all ip allows
    static final String MESSAGE_KEY = "message";

   /* public static void logMessage(String msg, String mobilenumber) {
        Connection con = DBPool.get();
        try {
            PreparedStatement ps = con.prepareStatement("insert into smslog (msg, mobilenumber) values (?,?);");
            ps.setString(1, msg);
            ps.setString(2, mobilenumber);
          //  ps.setString(3, result);
            ps.executeUpdate();
            con.close();
        } catch (Exception ex) {
            DBPool.log(ex);
        }

    }
*/
    
    public String getStringFromRequest(HttpServletRequest request) {
        
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = request.getReader();
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            DBPool.log(ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(GlobalClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sb.toString();
    }
    public static long CurrentTimeToN() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat prevDateFormat = new SimpleDateFormat("MMMM d, yyyy, HH:mm:ss z");
            long seconds = cal.getTimeInMillis() / 1000;
            Date prevDate = prevDateFormat.parse("January 1, 1980, 00:00:00 GMT+5:30");
            seconds = seconds - (prevDate.getTime() / 1000);
            return seconds;
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return 0;
    }

 

   /* public static String getUserCategory(String mobileno) {
        String category = "";
        try {
            Connection con = DBPool.get();
            PreparedStatement ps = con.prepareStatement("select category from NotificationRegId where mobileno = ?");
            ps.setString(1, mobileno);
            DBPool.log(ps.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                category = rs.getString(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(GlobalClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        return category;
    }
*/
    public static boolean insertUpdateUser(String regId, String imei, String mobileos) {

        Connection con = DBPool.get();

        mobileos ="ANDROID";// mobileos.equalsIgnoreCase("a") ? android : ios;

        String stmt = "update NotificationRegId set regId=? where imei =? and os=?";

        PreparedStatement ps;
        int result = 0;
        boolean res = false;
        try {
            
            
            ps = con.prepareStatement(stmt);
            ps.setString(1, regId);
            ps.setString(2, imei);
          //  ps.setString(3, mobileno);
            ps.setString(3, mobileos);
            result = ps.executeUpdate();
            DBPool.log("Update the registration details : "+ps.toString());
            DBPool.log("Result of the excuted query is : "+result);
        } catch (SQLException ex) {
            DBPool.log(ex);
            
        }
        if (result == 0) {
            try {
                ps = con.prepareStatement("insert into NotificationRegId(regid,imei,os) values (?,?,?);");
                //ps.setString(1, mobileno);
                ps.setString(1, regId);
                ps.setString(2, imei);
                ps.setString(3, mobileos);
                
                DBPool.log(ps.toString());
                result = ps.executeUpdate();
                if (result == 1) {
                    ArrayList<String> arrra = new ArrayList(1);
                    arrra.add(regId);
//                    DBPool.log("for "+userId+" "+sendMessagesForAndroid("Welcome To PFS",arrra));
                    con.close();
                    res = false;
                    return res;
                }
            } catch (Exception ex) {
                DBPool.log(ex);
            }

        } else {
            try {
                con.close();
            } catch (SQLException ex) {
                DBPool.log(ex);
            }
            res = true;
            return res;
        }

        return res;
    }

    public static String sendMessagesForAndroid(JSONObject msg, ArrayList<String> regid) {
        try {
            DBPool.log("Sending message for android " + regid.size() + " users");
            Result result = null;

            String msgret = "<br /> Result in ANDROID :";

            Sender sender = new Sender(GOOGLE_SERVER_KEY);
            int i = 0;
            int comp = 0, err = 0;
            for (String regid_val : regid) {
                long msgid = System.nanoTime();
                msg.put("id", msgid);
                try {
                    putforconf(msg, msgid);
                } catch (Exception ex) {
                    DBPool.log(ex);
                    msgret += ex.toString();
                }
                Message message = new Message.Builder().delayWhileIdle(true).addData(MESSAGE_KEY, msg.toJSONString()).build();
                //  msgret+="<br /> "+regid_val+" Result";
                try {
                    result = sender.send(message, regid_val, 5);
                    
                } catch (Exception ex) {
                    //msgret += ex.toString();
                    DBPool.log("Exception while sending...");
                    DBPool.log(ex);
                    err++;
                }
               

                if (result != null && result.getErrorCodeName() == null) {
                    comp++;
                    //  msgret += " Message Sending complete to : " +  + result.getErrorCodeName();
                } else {
                    DBPool.log(result.getErrorCodeName());
                    err++;
                    //  msgret += " Message Sending Error" + result.getErrorCodeName();
                }
                i++;
            }
            msgret += " Message Sending to " + i + " complete to : " + comp + " Error to :" + err;
            return msgret;

        } catch (Exception ioe) {
            DBPool.log(ioe);
            return "RegId required: in ANDROID  " + ioe.toString();
        }
    }
    
    public static String sendMessagesForAndroidM(JSONObject msg, ArrayList<String> regid, ArrayList<String> clCode) {
        try {
            DBPool.log("Sending message for android " + regid.size() + "users");
            Result result = null;

            String msgret = "<br /> Result in ANDROID :";

            Sender sender = new Sender(GOOGLE_SERVER_KEY);
            int i = 0;
            int comp = 0, err = 0;
            for (String regid_val : regid) {
                long msgid = System.nanoTime();
                System.out.println("msg time"+msgid);
                msg.put("id", msgid);
                try {
                    putforconf(msg, msgid);
                } catch (Exception ex) {
                    DBPool.log(ex);
                    msgret += ex.toString();
                }
                Message message = new Message.Builder().delayWhileIdle(true).addData(MESSAGE_KEY, msg.toJSONString()).build();
                //  msgret+="<br /> "+regid_val+" Result";
                try {
                    result = sender.send(message, regid_val, 5);
                    
                } catch (Exception ex) {
                    //msgret += ex.toString();
                    DBPool.log(ex);
                    err++;
                }
               

                if (result != null && result.getErrorCodeName() == null) {
                    comp++;
                    //  msgret += " Message Sending complete to : " +  + result.getErrorCodeName();
                } else {
                    DBPool.log(result.getErrorCodeName());
                    err++;
                    //  msgret += " Message Sending Error" + result.getErrorCodeName();
                }
                i++;
            }
            msgret += " Message Sending to " + i + " complete to : " + comp + " Error to :" + err;
            return msgret;

        } catch (Exception ioe) {
            DBPool.log(ioe);
            return "RegId required: in ANDROID  " + ioe.toString();
        }
    }

    private static void putforconf(JSONObject msg, long msgid) {
        Connection con = DBPool.get();
        try {
            DBPool.log("Put for conf message " + msg);
            String strMsg = (String) msg.get("msg");
            long clientCode = Long.parseLong("0000000000");
            //DBPool.log("ClientCode " + clientCode);

            PreparedStatement ps = con.prepareStatement("insert into NotificationMsgLog (msgid,message,mobileno) values (?,?,?);");//msgid,msg,timestamp
            ps.setLong(1, msgid);
            ps.setString(2, strMsg);
           // ps.setLong(3, clientCode);
            ps.executeUpdate();

            DBPool.log("Put for conf message --> Done");
        } catch (Exception ex) {
            DBPool.log(ex);
        }

        try {
            con.close();
        } catch (Exception ex) {

        }
    }

    static void DeliveryReport(long msgid) {
        Connection con = DBPool.get();
        try {
            PreparedStatement ps = con.prepareStatement("update NotificationMsgLog set status=? where msgid=?;");//msgid,msg,timestamp
            ps.setLong(1, new Date().getTime());
            ps.setLong(2, msgid);

            ps.executeUpdate();

        } catch (Exception ex) {
            DBPool.log(ex);
        }

        try {
            con.close();
        } catch (Exception ex) {

        }
    }

    
     public static void notifyToAll() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Connection con = DBPool.get();
                String message = "Test String";
                int id = 5;
                try{
                    CallableStatement cls = con.prepareCall(" call getNotificationIDS ("+id+")");
                    ResultSet rs = cls.executeQuery();
                    if(rs.next()){
                        message = rs.getString(1);
                        id = rs.getInt(2);
                    }
                }catch(Exception ex){
                    DBPool.log(ex);
                }
                
                
                JSONObject obj = new JSONObject();
                obj.put("message", message);
                obj.put("time", new java.util.Date().getTime());
                obj.put("pid", id);
                ArrayList<String> android = new ArrayList(10);
                ArrayList<String> ios = new ArrayList(10);
                ArrayList<String> clientNumber = new ArrayList(10);
                try {
                    String stmt = "select regId,os,mobileno from NotificationRegId order by os;";
                    ResultSet rs = con.createStatement().executeQuery(stmt);
                    while (rs.next()) {
                        if (rs.getString(2).equalsIgnoreCase(GlobalClass.android)) {
                            android.add(rs.getString(1));
                        } else {
                            ios.add(rs.getString(1));
                        }
                        //clientNumber.add(rs.getString(3));
                    }
                    con.close();
                } catch (Exception ex) {
                    DBPool.log(ex);
                }
                DBPool.log(GlobalClass.sendMessagesForAndroid(obj, android));
            }
        }
        ).start();

    }
    
   /* public static String getCount(int id){
        String return1 = "(0)";
        Connection con = DBPool.get();
        try {
            ResultSet rs = con.createStatement().executeQuery("select count(*) from marble where pcategory = "+id);
            if(rs.next()){
                return1 = "("+rs.getInt(1)+")";
            }
            rs.close();          
                    
        } catch (Exception ex) {
            DBPool.log(ex);
        }
        
        try {
            con.close();
        } catch (Exception ex) {

        }
        
        return return1;
    }

   */
}

    

