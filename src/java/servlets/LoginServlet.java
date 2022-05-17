package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.User;
import services.AccountService;

/**
 *
 * @author Alix
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        //check if the user has already logged if
        if (session.getAttribute("userEmail") != null) {
            String logout = request.getParameter("logout");
            if (logout != null) {
                //if action = logout, invalidate session and send log out msg to login jsp
                request.setAttribute("logoutM", true);
                session.invalidate();
                getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                return;
            } else {
                try {
                    //if not logout, redirect to inventory or admin
                    AccountService as = new AccountService();
                    String userEmail = (String) session.getAttribute("userEmail");

                    User user = as.get(userEmail);
                    if (user.getActive()) {
                        if (as.isAdmin(userEmail)) {
                            response.sendRedirect("admin");
                        }
                        response.sendRedirect("inventory");
                    } else {
                        request.setAttribute("errorM_inactive", true);
                        getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                        return;
                    }
                } catch (Exception ex) {
                    Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            //when userEmail session attribute is null. fist time log in
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String useremail = request.getParameter("user_email");
        String userpassword = request.getParameter("user_password");

        AccountService as = new AccountService();
        User user = null;
        request.setAttribute("user_Email", useremail); //if user input is invalid, the input info remained in jsp
        request.setAttribute("user_Password", userpassword);

        try {
            if (as.isAllFilled(useremail, userpassword)) { //check if all credentials are filled
                user = as.loginVerify(useremail, userpassword);
                if (user != null) { // check if user login credentials are correct
                    if (as.isActive(useremail)) { //check if the account is active
                        //A successful login creates a session object. Put  the username in the session
                        session.setAttribute("userEmail", useremail);
                        session.setAttribute("userObject", user);
                        if (as.isAdmin(useremail)) {//check if the account is Admin
                            response.sendRedirect("admin");
                            return;
                        } else {
                            response.sendRedirect("inventory");
                            return;
                        }
                    } else { //if account is not active, send error message to login jsp
                        request.setAttribute("errorM_inactive", true);
                    }
                } else {//if account is not valid, send error message to login jsp
                    request.setAttribute("errorM_invalid", true);
                }
            } else {
                request.setAttribute("errorM_missing_credentials", true);

            }
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        } catch (Exception ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
