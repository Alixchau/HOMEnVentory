package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.AccountService;

/**
 *
 * @author Alix
 */
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        if (uuid == null) {
            getServletContext().getRequestDispatcher("/WEB-INF/reset.jsp").forward(request, response);
            return;
        } else {
            request.setAttribute("uuid", uuid);
            getServletContext().getRequestDispatcher("/WEB-INF/resetPassword.jsp").forward(request, response);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountService as = new AccountService();
        String action = request.getParameter("action");
        //retrieve action parameter from reset.jsp and resetPassword.jsp doPOST methods
        if (action != null && action.equals("requestReset")) {
            //Send email to user retrieve email address from reset.jsp
            String url = request.getRequestURL().toString();
            String email = request.getParameter("email");
            String path = getServletContext().getRealPath("/WEB-INF");
            try {

                request.setAttribute("sentEmail", true);
                getServletContext().getRequestDispatcher("/WEB-INF/reset.jsp").forward(request, response);
                as.resetPassword(email, path, url);
                return;
            } catch (Exception ex) {
                Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (action != null && action.equals("savePassword")) {
            //change new password
            String uuid = request.getParameter("uuid");
            String password = request.getParameter("newPassword");
            Boolean changedPassword = as.changePassword(uuid, password);
            if (changedPassword) {
                request.setAttribute("changedPassword", true);
                getServletContext().getRequestDispatcher("/WEB-INF/resetPassword.jsp").forward(request, response);
                return;
            }
        }

    }

}
