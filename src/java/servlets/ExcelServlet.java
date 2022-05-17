package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.User;
import models.UserInventory;
import services.AccountService;
import services.ExcelService;
import services.InventoryService;

/**
 *
 * @author Alix
 */
public class ExcelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.getAttribute("userObject");
        ExcelService es = new ExcelService();
        Collection<UserInventory> userInventoryList = es.getAll();
        session.setAttribute("userInventoryList", userInventoryList);
        for (UserInventory userInventory : userInventoryList) {
            String username = userInventory.getUser().getEmail();
            String fn = userInventory.getUser().getFirstName();
            String ln = userInventory.getUser().getLastName();
            String rn = userInventory.getUser().getRole().getRoleName();
            boolean ac = userInventory.getUser().getActive();

        }

        getServletContext().getRequestDispatcher("/WEB-INF/exportToExcel.jsp").forward(request, response);
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        ExcelService es = new ExcelService();
        Collection<UserInventory> activeUsers = null;
        try {
            activeUsers = es.getAllActive();
        } catch (Exception ex) {
            Logger.getLogger(ExcelServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (action != null && action.equals("exportAll")) {
            getServletContext().getRequestDispatcher("/WEB-INF/excel_all.jsp").forward(request, response);
            return;
        } else if (action != null && action.equals("exportActive")) {
            session.setAttribute("activeUsers", activeUsers);
            getServletContext().getRequestDispatcher("/WEB-INF/excel_active.jsp").forward(request, response);
            return;
        }

    }

}
