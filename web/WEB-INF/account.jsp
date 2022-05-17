<%-- 
    Document   : account
    Created on : 26-Nov-2021, 11:13:23 PM
    Author     : Alix
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en-US">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Manage Account Page</title>
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
                    <li><a href="inventory">Inventory</a></li>
                    <li><a href="inventory?manageAccount">Manage User Account</a></li>
                    <li><a href="login?logout">Logout</a></li>
                    <br>
                    <li><a>Language:</a></li>
                </ul>
                    <div id="google_translate_element"></div>
            </div>
            <div class="contentPage">
                <h2>Manage User Information</h2>
                <div class="manageTable">
                    <ul>
                        <form action="inventory" method="POST" id="addTable">
                            <%-- if user updated user information, the mangeuserObject will not be null, this conditon is to show user info accordingly --%>
                            <c:if test="${manageuserObject == null}"> 
                                <li><input type="text" name="email" value="${userObject.email}" placeholder="Email"></li>
                                <li><input type="text" name="password" value="${userObject.password}" placeholder="Password"></li>
                                <li><input type="text" name="firstname" value="${userObject.firstName}" placeholder="First Name"></li>
                                <li><input type="text" name="lastname" value="${userObject.lastName}" placeholder="Last Name"></li>
                                <li><label>Active</label>
                                    <input type="checkbox" name="active" value="${userObject.active}"
                                           <c:if test="${userObject.active}">checked</c:if> >
                                    </li>
                            </c:if>
                            <c:if test="${manageuserObject != null}">
                                <li><input type="text" name="email" value="${manageuserObject.email}" placeholder="Email"></li>
                                <li><input type="text" name="password" value="${manageuserObject.password}" placeholder="Password"></li>      
                                <li><input type="text" name="firstname" value="${manageuserObject.firstName}" placeholder="First Name"></li>
                                <li><input type="text" name="lastname" value="${manageuserObject.lastName}" placeholder="Last Name"></li>
                                <li><label>Active</label>
                                    <input type="checkbox" name="active" value="${manageuserObject.active}"
                                           <c:if test="${manageuserObject.active}">checked</c:if> >
                                    </li>
                            </c:if> 
                            <li>
                                <input type="hidden" name="action" value="manage">
                                <input type="submit" value="Save">
                            </li>
                        </form>
                        <c:if test="${errorM_ManageUser}">
                            <p>Error. Please fill all fields</p>
                        </c:if>
                        <c:if test="${managedMsg}">
                            <p>You account has been updated successfully.</p>
                        </c:if>
                    </ul>
                </div>
            </div>
        </div>
    </body>
</html>
