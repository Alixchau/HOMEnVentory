<%-- 
    Document   : excel
    Created on : 2-Dec-2021, 9:18:53 PM
    Author     : Alix
--%>

<%@page import="models.UserInventory"%>
<%@page import="java.util.Collection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Active Users' Report</h1>
        <table cellpadding="1"  cellspacing="1" border="1" bordercolor="gray">
            <tr>
                <td>User Email</td>
                <td>First Name</td>
                <td>Last Name</td>
                <td>Role</td>
                <td>Active</td>
                <td>Number of Items</td>
                <td>Total Value</td>
            </tr>

            <%
                Collection<UserInventory> activeList = (Collection<UserInventory>) session.getAttribute("activeUsers");
                if (activeList != null) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "inline; filename = active_report.xls");
                }
                for (UserInventory al : activeList) {
            %>

            <tr>
                <td><%=al.getUser().getEmail()%></td>
                <td><%=al.getUser().getFirstName()%></td>
                <td><%=al.getUser().getLastName()%></td>
                <td><%=al.getUser().getRole().getRoleName()%></td>
                <td><%=al.getUser().getActive()%></td>
                <td><%=al.getNumberOfItems()%></td>
                <td><%=al.getValueOfItems()%></td>
            </tr>

            <%
                }
            %>

        </table>
    </body>
</html>
