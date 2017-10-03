

<%@page import="com.swadeshi.DBPool"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Swadeshi Application Log Page</title>
    </head>
    <body>
        <h1 style="color: green">SWADESHI ANDALAN Log Details...</h1>
        <%
            DBPool.printLog(out);
            %>
            <h6 style="color: green; alignment-adjust: central">End of Log</h6>    
    </body>
</html>
