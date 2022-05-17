<%-- 
    Document   : resetPassword
    Created on : 8-Dec-2021, 9:12:00 PM
    Author     : Alix
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="style.css">
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password</title>
    </head>
    <body>
        <h1>Reset password</h1>
        <c:if test="${changedPassword eq null}">
            <h1>Enter a new password</h1>
            <div class="manageTable">
                <form ation="reset" method="post">
                    <input type="password" name="newPassword" >
                    <input type="hidden" name="action" value="savePassword">
                    <input type="submit" value="Submit">
                </form>
            </c:if>
            <c:if test="${changedPassword}">
                <ul><li> <p>The password has been reset.</p></li></ul>
                    </c:if>
        </div>
    </body>
</html>
