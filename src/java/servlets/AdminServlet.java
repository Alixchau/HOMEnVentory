package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Category;
import models.Item;
import models.User;
import services.AccountService;
import services.InventoryService;

/**
 *
 * @author Alix
 */
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        AccountService as = new AccountService();
        InventoryService is = new InventoryService();
        String action = request.getParameter("action");
        String logout = request.getParameter("logout");
        try {
            String userEmail = (String) session.getAttribute("userEmail");
            if (userEmail != null && as.isAdmin(userEmail)) {//check if user is admin
                if (action == null) {
                    try { //show all users
                        Collection<User> userList = as.getAll();
                        Collection<Category> CategoryList = is.getAllCat();
                        session.setAttribute("userList", userList);
                        session.setAttribute("categoryList", CategoryList);
                    } catch (Exception ex) {
                        Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (action != null && action.equals("edit")) {//if edit is clicked, retrieve selected user info
                    User user = null;
                    try {
                        String userPK = request.getParameter("userPK");
                        user = as.get(userPK);
                        request.setAttribute("SelectedUser", user);
                    } catch (Exception ex) {
                        Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (action != null && action.equals("editCat")) {//if edit category is clicked, retrieve selected category info
                    int catPK = Integer.parseInt(request.getParameter("catPK"));
                    Category cat = is.getCat(catPK);
                    request.setAttribute("SelectedCat", cat);
                } else if(action != null && action.equals("search")){//if seach item is clicked
                    String searchName = request.getParameter("searchItem");
                    Collection<Item> searchItems = is.getSearchItem(searchName);
                    request.setAttribute("searchItems", searchItems);
                }
                getServletContext().getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
                return;
            } else if (userEmail != null && !as.isAdmin(userEmail)) { //if user is regular user
                response.sendRedirect("inventory");
            } else {
                response.sendRedirect("login");
            }
        } catch (Exception ex) {
            Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountService as = new AccountService();
        InventoryService is = new InventoryService();
        String action = request.getParameter("action");

        if (action.equals("add")) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String active = request.getParameter("active");
            int role_id = Integer.parseInt(request.getParameter("role"));
            boolean isActive = true;
            if (active == null) {
                isActive = false;
            }
            try {
                if (as.isAllFilledManageUser(email, firstname, lastname, password)) {
                    as.insert(email, isActive, firstname, lastname, password, role_id);
                } else { //display error msg and input data remain in admin jsp
                    request.setAttribute("errorM_AddUser", true);

                    request.setAttribute("user_password", password);
                    request.setAttribute("user_email", email);
                    request.setAttribute("user_firstname", firstname);
                    request.setAttribute("user_lastname", lastname);
                    request.setAttribute("user_role", role_id);
                    request.setAttribute("user_active", isActive);
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (action.equals("update")) {
            String email = request.getParameter("UpdateEmail");
            String password = request.getParameter("UpdatePassword");
            String firstname = request.getParameter("UpdateFirstname");
            String lastname = request.getParameter("UpdateLastname");
            String active = request.getParameter("UpdateActive");
            int role_id = Integer.parseInt(request.getParameter("Updaterole"));
            boolean isActive = true;
            if (active == null) {
                isActive = false;
            }

            try {
                if (as.isAllFilledManageUser(email, firstname, lastname, password)) {
                    as.update(email, isActive, firstname, lastname, password, role_id);
                } else {
                    User UpdateUser = new User(email, isActive, firstname, lastname, password);
                    request.setAttribute("SelectedUser", UpdateUser);
                    request.setAttribute("errorM_EditUser", true);
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("delete")) {
            try {
                String userEmail = (String) session.getAttribute("userEmail");
                String userPK = request.getParameter("userPK");
                if (as.isAdminSelf(userEmail, userPK)) {
                    request.setAttribute("errorM_deleteAdminSelf", true);
                } else {
                    as.delete(userPK);
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("updateCat")) {
            try {
                int catId = Integer.parseInt(request.getParameter("updateCatId"));
                String catName = request.getParameter("updateCatName");
                is.updateCategory(catId, catName);
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(action.equals("addCat")){
            try {
                String catName = request.getParameter("addCateName");
                is.addCategory(catName);
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collection<User> userList = null;
        Collection<Category> catList = null;
        try {
            userList = as.getAll();
            catList = is.getAllCat();
        } catch (Exception ex) {
            Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setAttribute("userList", userList);
        session.setAttribute("categoryList", catList);
        getServletContext().getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
        return;
    }

}
