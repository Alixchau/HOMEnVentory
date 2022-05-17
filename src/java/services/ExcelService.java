package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;
import models.UserInventory;

/**
 *
 * @author Alix
 */
public class ExcelService {

    public Collection<UserInventory> getAll() {

        Collection<UserInventory> userInventoryList = new ArrayList();

        AccountService as = new AccountService();
        InventoryService is = new InventoryService();
        Collection<User> userList = null;
        int numberOfItems;
        double valueOfItems;
        try {
            userList = as.getAll();
            for (User u : userList) {
                numberOfItems = is.getNumberOfItems(u);
                valueOfItems = is.getValueOfItems(u);
                UserInventory ui = new UserInventory(u, numberOfItems, valueOfItems);
                userInventoryList.add(ui);
            }
        } catch (Exception ex) {
            Logger.getLogger(ExcelService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userInventoryList;
    }
    
    public Collection<UserInventory> getAllActive(){
        
        Collection<UserInventory> userInventoryList = new ArrayList();

        AccountService as = new AccountService();
        InventoryService is = new InventoryService();
        Collection<User> userList = null;
        int numberOfItems;
        double valueOfItems;
        try {
            userList = as.getAllActive();
            for (User u : userList) {
                numberOfItems = is.getNumberOfItems(u);
                valueOfItems = is.getValueOfItems(u);
                UserInventory ui = new UserInventory(u, numberOfItems, valueOfItems);
                userInventoryList.add(ui);
            }
        } catch (Exception ex) {
            Logger.getLogger(ExcelService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userInventoryList;
    }

}
