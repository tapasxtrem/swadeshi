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
        if (session.getAttribute("userid").toString().equals("xtrem")) {

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
<%
     Connection conn = DBPool.get();
     String stmt1 = "";
            stmt1 = "select distinct mobileno from NotificationRegId where mobileno<>'0000000000' and mobileno<>'' order by os;";
            DBPool.log(stmt1);
            ResultSet rs1 = conn.createStatement().executeQuery(stmt1);            
%>

<%
             String stmt2 = "";
             stmt2 = "select name from aarti_image_list order by name;";
             DBPool.log(stmt2);
             ResultSet rs2 = conn.createStatement().executeQuery(stmt2);            
%>


<form method="post" action="notification.jsp">
    <textarea cols="40" rows="4"  placeholder="Type your message here..." id="message" name="message" required></textarea><br />
    <br>
    <select name="type">
        <option value="2">Aarti</option>
        <option value="1">Pravachan</option>
    </select>
    <select name="mobile">
        <option value="0">All</option> 
       <option value="1">All Registered Users</option>
                        <%  while (rs1.next()) {%>
                        <option value= <%= rs1.getString(1)%>><%= rs1.getString(1)%></option>
                        <% }%>
    </select>
    
    <select name="Category">
        <option value="pravachan">pravachan</option> 
        <%  while (rs2.next()) {%>
        <option value= "<%= rs2.getString(1)%>"><%= rs2.getString(1)%></option>
        <% }%>
    </select>
    
    <input type="submit" name="Submit" class="login login-submit" value="Submit" />

    <br/><br/>
</form>
<%
    //String APPLE_KEYSTORE_DIR_PATH = "maximum.htm";
    //com.xtrem.Marble.GlobalClass.APPLE_KEYSTORE_DIR_PATH = getServletContext().getResourceAsStream(APPLE_KEYSTORE_DIR_PATH);
    String message = request.getParameter("message");
    String mobileno = request.getParameter("mobile");
    String cate = request.getParameter("Category");
    if (message != null && message.trim().length() != 0) {
       // String clientcode = request.getParameter("clientcode");
     //   clientcode = clientcode.replaceAll(','+"" ,"','");
        JSONObject obj = new JSONObject();
        obj.put("msg", message);
        obj.put("type",request.getParameter("type"));
        obj.put("cate",cate);
        obj.put("time", new java.util.Date().getTime());
        message = obj.toJSONString();
        Connection con = DBPool.get();
        ArrayList<String> android = new ArrayList(10);
        ArrayList<String> ios = new ArrayList(10);
        ArrayList<String> clientNumber = new ArrayList(10);
        try {
            String stmt = "";
            if(mobileno.equalsIgnoreCase("1")){
              stmt = "select distinct regId,os,mobileno from NotificationRegId where mobileno<>'0000000000' and mobileno<>' ' order by os;";                                
            }
            else if(mobileno.equalsIgnoreCase("0")){
              stmt = "select distinct regId,os,mobileno from NotificationRegId where regId <> '' order by os;";                                  
            }
            else{
              stmt = "select distinct regId,os,mobileno from NotificationRegId where mobileno = '"+mobileno+"';";
            }
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
        out.println("<br />"+GlobalClass.sendMessagesForAndroidM(obj, android, clientNumber));
    }
%>
