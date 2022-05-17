package servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import models.Category;
import models.Item;
import models.User;
import services.AccountService;
import services.InventoryService;

/**
 *
 * @author Alix
 */
public class InventoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        InventoryService is = new InventoryService();
        AccountService as = new AccountService();
        String userEmail = (String) session.getAttribute("userEmail");
        String manageAccount = request.getParameter("manageAccount");
        if (userEmail != null) {

            try {
                //show all items belong to the user
                Collection<Category> catList = null;
                Collection<Item> itemList = null;
                User user = null;

                itemList = is.getAll(userEmail);
                catList = is.getAllCat();
                user = as.get(userEmail);

                if (user.getActive() == false) {
                    request.setAttribute("errorM_inactive", true);
                    response.sendRedirect("login");
                }
                //display info on the inventory jsp
                session.setAttribute("itemList", itemList);
                session.setAttribute("catList", catList);
                session.setAttribute("userObject", user);

                //if edit button is clicked
                String action = request.getParameter("action");
                if (action != null && action.equals("selectEdit")) {
                    int editItemID = Integer.parseInt(request.getParameter("itemPK"));
                    Item item = is.getItem(editItemID);
                    request.setAttribute("selectedItem", item);
                }
            } catch (Exception ex) {
                Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            //if user clicks manage user account, if yes go to account.jsp
            if (manageAccount != null) {
                getServletContext().getRequestDispatcher("/WEB-INF/account.jsp").forward(request, response);
                return;
            }
        } else {
            response.sendRedirect("login");
        }

        getServletContext().getRequestDispatcher("/WEB-INF/inventory.jsp").forward(request, response);
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        InventoryService is = new InventoryService();
        AccountService as = new AccountService();
        String action = request.getParameter("action");
        String userEmail = (String) session.getAttribute("userEmail");

        if (action.equals("add")) {
            String itemname = request.getParameter("itemname");
            String itemprice_string = request.getParameter("itemprice");
            if (itemname.equals("") || itemprice_string.equals("")) { //check if item name & price is empty
                request.setAttribute("errorM_addItem", true);
            }
            try {
                int categoryid = Integer.parseInt(request.getParameter("categoryid"));
                double itemPrice = Double.parseDouble(itemprice_string);
                is.addItem(itemname, itemPrice, userEmail, categoryid);
            } catch (Exception ex) {
                Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("delete")) {
            int deleteItemID = Integer.parseInt(request.getParameter("itemPK"));
            try {
                if (is.isTheOwner(userEmail, deleteItemID)) {//check if the user is the owner of the item
                    is.deleteItem(deleteItemID);
                } else {
                    request.setAttribute("errorM_deleteItem", true);
                }
            } catch (Exception ex) {
                Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("manage")) {

            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String active = request.getParameter("active");
            int role_id = 2; //regular user role is 2
            boolean isActive = true;
            if (active == null) {
                isActive = false;
            }

            try {
                if (as.isAllFilledManageUser(email, firstname, lastname, password)) {
                    as.update(email, isActive, firstname, lastname, password, role_id);
                    User user = new User(email, isActive, firstname, lastname, password);
                    session.setAttribute("manageuserObject", user); //apply to filter to check if the user is still active
                    getServletContext().getRequestDispatcher("/WEB-INF/account.jsp").forward(request, response);
                    return;
                } else {
                    User user = new User(email, isActive, firstname, lastname, password);
                    session.setAttribute("user", user);
                    request.setAttribute("errorM_ManageUser", true);
                    getServletContext().getRequestDispatcher("/WEB-INF/account.jsp").forward(request, response);
                    return;
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("edit")) {
            int itemId = Integer.parseInt(request.getParameter("editItemId"));
            String itemName = request.getParameter("editItemName");
            String itemPrice_string = request.getParameter("editItemPrice");
            if (itemName.equals("") || itemPrice_string.equals("")) { //check if item name & price is empty
                request.setAttribute("errorM_editItem", true);
            }
            try {
                int categoryid = Integer.parseInt(request.getParameter("categoryid_edit"));
                double itemPrice = Double.parseDouble(itemPrice_string);
                is.updateItem(itemId, itemName, itemPrice, categoryid);
            } catch (Exception ex) {
                Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("uploadPhoto")) {
            System.out.println("in post method.");
            Part file = request.getPart("image"); //get the image in the form of Part
            String imageFileName = file.getSubmittedFileName(); //get the file's filename 
            File f = (File) file;
            BufferedImage image = ImageIO.read(f);
            File outputfile = new File("/images/" + imageFileName);
            ImageIO.write(image, "jpg", outputfile);
            //String uploadPath = getServletContext().getResourcePaths("/images/") + imageFileName;
            //String uploadPath = "/images/" + imageFileName; //upload path is folder + filename

            // FileOutputStream fos = new FileOutputStream(uploadPath); //write the image to the uploadParth
            //InputStream fileIS = file.getInputStream();
            // byte[] data = new byte[fileIS.available()]; //get the number of bytes from the image's inputStream
            //fileIS.read(data); //read the image's bytes
            //fos.write(data); //write the image's bytes to the upload path (save)
            //fos.close();
        }
        Collection<Item> itemList = null;
        try {
            itemList = is.getAll(userEmail);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setAttribute("itemList", itemList);

        getServletContext().getRequestDispatcher("/WEB-INF/inventory.jsp").forward(request, response);
        return;

    }

}
