<%-- 
    Document   : reset
    Created on : 8-Dec-2021, 9:51:26 PM
    Author     : Alix
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="style.css">
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset password</title>
    </head>
    <body>
        <h1>Reset Password</h1>
        <div class="manageTable">
            <c:if test="${sentEmail eq null}">
                <p>Please enter your email address to reset your password.</p>
                <form action="reset" method="post">      
                    <label>Email address: </label>
                    <input type="text" name="email" value="">
                    <input type="hidden" name="action" value="requestReset">
                    <input type="submit" value="Submit">
                </form>
            </c:if>
            <c:if test="${sentEmail}">
                <ul><li> <p>If the address you entered is valid, you will receive an email to reset your password.</p></li></ul>
                    </c:if>

        </div>
    </body>
</html>
