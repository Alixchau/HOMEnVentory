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
        <h1>All Users' Report</h1>
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
                Collection<UserInventory> uiList = (Collection<UserInventory>) session.getAttribute("userInventoryList");
                if (uiList != null) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "inline; filename = all_report.xls");
                }
                for (UserInventory ui : uiList) {
            %>

            <tr>
                <td><%=ui.getUser().getEmail()%></td>
                <td><%=ui.getUser().getFirstName()%></td>
                <td><%=ui.getUser().getLastName()%></td>
                <td><%=ui.getUser().getRole().getRoleName()%></td>
                <td><%=ui.getUser().getActive()%></td>
                <td><%=ui.getNumberOfItems()%></td>
                <td><%=ui.getValueOfItems()%></td>
            </tr>


            <%
                }
            %>

        </table>
    </body>
</html>
