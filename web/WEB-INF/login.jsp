<%-- 
    Document   : login
    Created on : 11-Nov-2021, 9:40:45 PM
    Author     : Alix
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" href="style.css">
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
    </head>
    <body>
        <div class="container">
            <h1>HOME nVentory</h1>
            <div class="manageTable">
                <h3>Login</h3>
                <br>
                <form method="post">
                    <label>User Email:</label>
                    <input type="text" name="user_email" value="${user_Email}">
                    <br>
                    <label>Password:</label>
                    <input type="password" name="user_password" value="${user_Password}">
                    <br>
                    <input type="submit" value="Log in">
                </form>
                <ul><li><a href="reset">Forgot Password</a></li></ul>
                <c:if test="${logoutM}">
                    <p>You're successfully logged out.</p>
                </c:if>
                <c:if test="${errorM_inactive}">
                    <p>Sorry. This account is inactive.</p> 
                </c:if >
                <c:if test="${errorM_invalid}">
                    <p>Sorry. Invalid credential, please try again.</p>
                </c:if>
                <c:if test="${errorM_missing_credentials}">
                    <p>Sorry. Missing Credentials, please try again.</p>
                </c:if>
            </div>
            <br>
            <div class="manageTable">
                <h3>New User</h3>
                <ul>
                    <li><a href="register">Register</a></li>
                </ul>
            </div>
        </div>
    </body>
</html>
