<%-- 
    Document   : register
    Created on : 26-Nov-2021, 10:09:57 PM
    Author     : Alix
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="style.css">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Page</title>
    </head>
    <body>
        <div class="manageTable">
            <h3>Menu</h3>
            <ul>
                <li><a href="login">Back to Login</a></li>
            </ul>
        </div>
        <div class="manageTable">
            <c:if test="${accountActivated eq null}">
                <h3>User Information</h3>
                <ul>
                    <form action="register" method="POST" id="addTable">
                        <li><input type="text" name="email" value="${user_email}" placeholder="Email"></li>
                        <li><input type="text" name="firstname" value="${user_firstname}" placeholder="First Name"></li>
                        <li><input type="text" name="lastname" value="${user_lastname}" placeholder="Last Name"></li>
                        <li><input type="text" name="password" value="${user_password}" placeholder="Password"></li>
                        <li>
                            <input type="hidden" name="action" value="add">
                            <input type="submit" value="Save">
                        </li>
                    </form>


                    <c:if test="${errorM_AddUser}">
                        <p>Error. Please fill all fields</p>
                    </c:if>
                </c:if>
                <c:if test="${registeredMsg}">
                    <p>You're registered. An activation email has been sent to your email.</p>
                </c:if>
                <c:if test="${accountActivated}">
                    <p>Your account is activated.</p>   
                </c:if>
            </ul>
        </div>
    </body>
</html>
