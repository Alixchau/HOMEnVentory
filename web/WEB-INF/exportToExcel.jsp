<%-- 
    Document   : exportToExcel
    Created on : 2-Dec-2021, 3:50:19 PM
    Author     : Alix
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Export To Excel</title>
        <script type="text/javascript">
            function googleTranslateElementInit() {
                new google.translate.TranslateElement({pageLanguage: 'en'}, 'google_translate_element');
            }
        </script>
        <script type="text/javascript" src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
    </head>
    <body>
        <div class="container">
            <div class="sidenav">
                <h3>HOME nVentory</h3>
                <h4>Welcome, ${userObject.firstName} ${userObject.lastName}</h4>
                <ul>
                    <li><a href="admin">Admin</a></li>
                    <li><a href="inventory">Inventory</a></li>
                    <li><a href="ExportToExcel">Export To Excel</a></li>
                    <li><a href="login?logout">Logout</a></li>
                    <br>
                    <li><a>Language:</a></li>
                </ul>
                <div id="google_translate_element"></div>
            </div>
            <div class="contentPage">
                <h2>Export To Excel</h2>
                <div class="AdminManageTable">

                    <ul>
                        <li>
                            <span>User Email</span>
                            <span>First Name</span>
                            <span>Last Name</span>
                            <span>Role</span>
                            <span>Active</span>
                            <span>Number of Items</span>
                            <span>Total Value</span>
                        </li>
                        <c:forEach var="userInventory" items="${userInventoryList}">
                            <li>
                                <span>${userInventory.getUser().getEmail()}</span>
                                <span>${userInventory.getUser().getFirstName()}</span>
                                <span>${userInventory.getUser().getLastName()}</span>
                                <span>${userInventory.getUser().getRole().getRoleName()}</span>
                                <span>
                                    <input type="checkbox" value="${userInventory.getUser().getActive()}"
                                           <c:if test="${userInventory.getUser().getActive() == true}">checked</c:if> >                                          
                                    </span>
                                    <span>${userInventory.getNumberOfItems()}</span>
                                <span>${userInventory.getValueOfItems()}</span>
                            </li>
                        </c:forEach>   
                    </ul>
                    <form action="ExportToExcel" method="POST">
                        <ul>
                            <li><input type="submit" value="Export All Users">
                                <input type="hidden" name="action" value="exportAll">
                            </li>
                        </ul>
                    </form>
                    <form action="ExportToExcel" method="POST">
                        <ul>
                            <li><input type="submit" value="Export Active Users">
                                <input type="hidden" name="action" value="exportActive">
                            </li>
                        </ul>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
