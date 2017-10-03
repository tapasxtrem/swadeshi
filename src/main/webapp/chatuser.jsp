<%-- 
    Document   : chatuser
    Created on : Dec 5, 2016, 12:55:53 PM
    Author     : XtremsoftTechnologies
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.CallableStatement"%>
<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.swadeshi.DBPool"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chat Users Page</title>
    </head>
    <body>
        <table border="1" >
            <%
                Connection con = null;
                CallableStatement cls = null;
                ResultSet rs = null;
                try {
                    con = DBPool.get();
                    cls = con.prepareCall("{call Proc_GetChatUser}");
                    rs = cls.executeQuery();
                    ResultSetMetaData mda = rs.getMetaData();

                    int cc = mda.getColumnCount();
                    out.println("<thead><tr>");
                    for (int i = 0; i < cc; i++) {
                        String lbl = mda.getColumnLabel(i + 1);
                        out.println("<th>" + lbl);
                        out.println("</th>");
                    }
                    out.println("</tr></thead>");
                    int counter = 0;
                    while (rs.next()) {

                        out.println("<tr " + (counter % 2 == 0 ? "" : "class='alt'") + ">");
                        counter++;
                        out.println("<td>");
                        String mobNo = rs.getString(1);
                        out.println(mobNo);
                        out.println("</td>");
                        out.println("<td>");
                        out.println(rs.getString(2));
                        out.println("</td>");
                        out.println("<td>");
                        out.println(rs.getString(3));
                        out.println("</td>");
                        out.println("<td>");
                        boolean flag = rs.getBoolean(4);
                        //out.println("<input type='checkbox'  checked='"+flag+"'>");//);
                        if (flag) {
                            out.println("<input type='checkbox' id='" + mobNo + "' "
                                    + " checked='checked' onclick='handleClick(this);' >");//);
                        } else {
                            out.println("<input type='checkbox' id='" + mobNo + "' "
                                    + "onclick='handleClick(this);'>");
                        }
                        out.println("</td>");
                        /*for(int i=0;i<cc;i++)
                         {
                         out.println("<td>");
                         out.println(rs.getString(i+1));
                         out.println("</td>");
                         }*/
                        out.println("</tr>");

                    }
                    // out.println("</tbody>");

                } catch (Exception ex) {
                    out.println(ex.toString());
                    DBPool.log(ex);
                } finally {
                    DBPool.close(con, cls, rs);
                }


            %>

        </table>
        <script>
            function handleClick(cb) {
                var id = cb.id;
                var active = cb.checked;
             location.href ="/swadeshi/ActiveDeactive?id="+id+"&active="+active;
            }
        </script>
    </body>
</html>
