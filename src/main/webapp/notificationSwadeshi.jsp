<%-- 
    Document   : home
    Created on : 12 Dec, 2014, 6:54:01 PM
    Author     : XPC17
--%>

<%@page import="com.swadeshi.GlobalClass"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.swadeshi.DBPool"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    try {
        if (session.getAttribute("userid").toString().equals("xtrem") ) {

        } else {
            response.sendRedirect("index.jsp");
            //out.println("Invalid Login <a href='index.jsp'>try again</a>");
            return;
        }
    } catch (java.lang.NullPointerException ex) {
        response.sendRedirect("index.jsp");
        //out.println("Invalid Login <a href='index.jsp'>try again</a>");
        return;
    }

%>

<script>
    function chkNumeric(evt) {
        evt = (evt) ? evt : window.event;
        var charCode = (evt.which) ? evt.which : evt.keyCode;
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
            if (charCode === 46) {
                return true;
            }
            else {
                return false;
            }
        }
        return true;
    }
</script>

<form method="post" action="notificationSwadeshi.jsp">
    <textarea cols="40" rows="4"  placeholder="Type your message here..." id="message" name="message" required></textarea><br/>
    <br>
    <select name="type">
        <option value="1">Notification</option>
      
    </select>
    <input type="submit" name="Submit" class="login login-submit" value="Submit" />

    <br/><br/>
</form>
<%
    //String APPLE_KEYSTORE_DIR_PATH = "maximum.htm";
    //com.xtrem.Marble.GlobalClass.APPLE_KEYSTORE_DIR_PATH = getServletContext().getResourceAsStream(APPLE_KEYSTORE_DIR_PATH);
    String message = request.getParameter("message");
   
    if (message != null && message.trim().length() != 0) {
       // String clientcode = request.getParameter("clientcode");
     //   clientcode = clientcode.replaceAll(','+"" ,"','");
        JSONObject obj = new JSONObject();
        obj.put("msg", message);
        obj.put("type",request.getParameter("type"));
        obj.put("time", new java.util.Date().getTime());
        message = obj.toJSONString();
        Connection con = DBPool.get();
        ArrayList<String> android = new ArrayList(10);
        ArrayList<String> ios = new ArrayList(10);
        ArrayList<String> clientNumber = new ArrayList(10);
        try {
            String stmt = "";
            stmt = "select distinct regId,os from NotificationRegId order by os;";
            DBPool.log(stmt);
            ResultSet rs = con.createStatement().executeQuery(stmt);

            while (rs.next()) {
                if (rs.getString(2).equalsIgnoreCase(GlobalClass.android)) {
                    android.add(rs.getString(1));
                } else {
                    ios.add(rs.getString(1));
                }
                clientNumber.add(rs.getString(3));
            }

            con.close();
        } catch (Exception ex) {
            DBPool.log(ex);
        }
        out.println("<br/>"+GlobalClass.sendMessagesForAndroidM(obj, android, clientNumber));
    }
%>
