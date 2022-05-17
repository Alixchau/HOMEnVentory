package servlets;

import dataaccess.UserDB;
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
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        HttpSession session = request.getSession();
        if (uuid != null) {
            try {
                AccountService as = new AccountService();
                UserDB userDB = new UserDB();
                User user = userDB.getByRegisterUUID(uuid);
                String email = user.getEmail(); //retrieve user's email by register uuid
                String path = getServletContext().getRealPath("/WEB-INF");
                as.welcomeEmail(email, path);//send a welcome email to user
                as.setUserActive(uuid); //set user's status to active from defult false, and set uuid void
                request.setAttribute("accountActivated", true);
                session.setAttribute("userEmail", null); //clear session, back to login page. otherwise too many redirect in login servlet

            } catch (Exception ex) {
                Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        getServletContext().getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
        return;

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountService as = new AccountService();
        String action = request.getParameter("action");
        User user = null;
        if (action.equals("add")) {
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            boolean isActive = false; //default active stauts when register new user. activate email will sent to user later
            int role_id = 2; //New users have a “regular user” role
            try {
                if (as.isAllFilledManageUser(email, firstname, lastname, password)) {
                    as.insert(email, isActive, firstname, lastname, password, role_id);

                    //send email to user to activate the account
                    String url = request.getRequestURL().toString();
                    String path = getServletContext().getRealPath("/WEB-INF");
                    as.activateAccount(email, path, url);

                    user = as.loginVerify(email, password);
                    session.setAttribute("userObject", user); //adminFilter will assess the user object is admin or not
                    request.setAttribute("registeredMsg", true);
                    session.setAttribute("userEmail", email); //attribute to redirect to inventory page do get

                } else { //display error msg and input data remain in admin jsp
                    request.setAttribute("errorM_AddUser", true);
                    request.setAttribute("user_password", password);
                    request.setAttribute("user_email", email);
                    request.setAttribute("user_firstname", firstname);
                    request.setAttribute("user_lastname", lastname);
                    getServletContext().getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
                    return;
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            getServletContext().getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
            return;
        }

    }

}
