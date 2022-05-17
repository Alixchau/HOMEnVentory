package services;

import dataaccess.RoleDB;
import dataaccess.UserDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import models.Role;
import models.User;

/**
 *
 * @author Alix
 */
public class AccountService {

    public User loginVerify(String email, String password) throws Exception {
        UserDB userDB = new UserDB();

        try {
            User user = userDB.get(email);
            if (user.getPassword().equals(password)) {
                return user;
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public Collection<User> getAll() throws Exception {
        UserDB userDB = new UserDB();
        Collection<User> users = userDB.getAll();
        return users;
    }

    public Collection<User> getAllActive() throws Exception {
        UserDB userDB = new UserDB();
        AccountService as = new AccountService();
        Collection<User> users = userDB.getAll();
        Collection<User> activeUsers = new ArrayList();
        for (User u : users) {
            if (u.getActive()) {
                activeUsers.add(u);
            }
        }
        return activeUsers;
    }

    public User get(String email) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        return user;
    }

    public boolean isAllFilled(String email, String password) throws Exception {
        if (email != null && !email.equals("") && password != null && !password.equals("")) {
            return true;
        }
        return false;
    }

    public boolean isAllFilledManageUser(String email, String firstname, String lastname, String password) throws Exception {
        if (email == null || email.equals("") || password == null
                || password.equals("") || firstname == null || firstname.equals("")
                || lastname == null || lastname.equals("")) {
            return false;
        }
        return true;
    }

    public Role getRole(String username) throws Exception {

        UserDB userDB = new UserDB();
        User user = userDB.get(username);

        return user.getRole();
    }

    public boolean isActive(String username) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(username);

        return user.getActive();
    }

    public void insert(String email, boolean active, String firstName, String lastName, String password, int role_id) throws Exception {
        User user = new User(email, active, firstName, lastName, password);

        RoleDB roleDB = new RoleDB();
        Role role = roleDB.get(role_id); //create role by its primary key role_id
        user.setRole(role); // update user's role attribute by setRole method in user class. role is the reference key to role table

        UserDB userDB = new UserDB();
        userDB.insert(user); //insert into user table
    }

    public void update(String email, boolean active, String firstName, String lastName, String password, int role_id) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(email);//retrieve the user by PK
        user.setActive(active);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);

        RoleDB roledb = new RoleDB();
        Role role = roledb.get(role_id);
        user.setRole(role);

        userDB.update(user);
    }

    public void delete(String email) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        userDB.delete(user);
    }

    public boolean isAdminSelf(String username, String deleteUserPK) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(username);
        Role role = user.getRole();
        //check if the user is trying to delete admin themselves
        if (username.equals(deleteUserPK) && (role.getRoleId() == 1 || role.getRoleId() == 3)) {
            return true;
        }
        return false;
    }

    public boolean isAdmin(String email) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        Role role = user.getRole();
        if (role.getRoleId() == 1 || role.getRoleId() == 3) {
            return true;
        }
        return false;
    }

    public void resetPassword(String email, String path, String url) throws Exception {
        //Create a UUID and store in database for the user
        String uuid = UUID.randomUUID().toString();
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        user.setResetPasswordUuid(uuid);
        userDB.update(user);

        String to = user.getEmail();
        String subject = "Reset Password for HOME nVentory";
        String resetTemplate = path + "/emailTemplate/resetPassword.html";

        String link = url + "?uuid=" + uuid;

        HashMap<String, String> tags = new HashMap<>();
        tags.put("firstname", user.getFirstName());
        tags.put("lastname", user.getLastName());
        tags.put("link", link);

        GmailService.sendMail(to, subject, resetTemplate, tags);
    }

    public boolean changePassword(String uuid, String password) {
        UserDB userDB = new UserDB();
        try {
            User user = userDB.getByResetPasswordUUID(uuid);
            user.setPassword(password);
            user.setResetPasswordUuid(null); //set uuid back to null
            userDB.update(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    //send a activation email to user
    public void activateAccount(String email, String path, String url) throws Exception {
        //Create a UUID (universally unique ID) and store in database for the user.
        String uuid = UUID.randomUUID().toString();
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        user.setRegisterUuid(uuid);
        userDB.update(user);

        String to = user.getEmail();
        String subject = "Active account for HOME nVentory";
        String activateTemplate = path + "/emailTemplate/activateAccount.html";

        String link = url + "?uuid=" + uuid;
        HashMap<String, String> tags = new HashMap<>();
        tags.put("firstname", user.getFirstName());
        tags.put("lastname", user.getLastName());
        tags.put("link", link);

        GmailService.sendMail(to, subject, activateTemplate, tags);
    }
    //after activation, user becomes active
    public void setUserActive(String uuid) {
        UserDB userDB = new UserDB();
        try {
            User user = userDB.getByRegisterUUID(uuid);
            user.setActive(true);
            user.setRegisterUuid(null); //set uuid back to null
            userDB.update(user);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void welcomeEmail(String email, String path) throws Exception {
        String to = email;
        String subject = "Welcome for HOME nVentory";
        String activateTemplate = path + "/emailTemplate/welcome.html";

        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        HashMap<String, String> tags = new HashMap<>();
        tags.put("firstname", user.getFirstName());
        tags.put("lastname", user.getLastName());
        //send welcome email
        GmailService.sendMail(to, subject, activateTemplate, tags);
    }
}
