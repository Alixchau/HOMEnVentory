package services;

import dataaccess.CategoriesDB;
import dataaccess.ItemsDB;
import dataaccess.UserDB;
import java.util.ArrayList;
import java.util.Collection;
import models.Category;
import models.Item;
import models.User;

/**
 *
 * @author Alix
 */
public class InventoryService {

    //list the items collections under the user
    public Collection<Item> getAll(String username) throws Exception {
        ItemsDB itemsDB = new ItemsDB();
        Collection<Item> items = itemsDB.getAll(username);
        return items;
    }
    
    //list the items contains the searched item name
    public Collection<Item> getSearchItem(String searchName) throws Exception{
        ItemsDB itemsDB = new ItemsDB();
        Collection<Item> searchItems = itemsDB.getSearchItems(searchName);
        return searchItems;
    }

    public Collection<Category> getAllCat() throws Exception {
        CategoriesDB catDB = new CategoriesDB();
        Collection<Category> catList = catDB.getAll();
        return catList;
    }

    //get the selected category
    public Category getCat(int catId) throws Exception {
        CategoriesDB catDB = new CategoriesDB();
        Category cat = catDB.getCategory(catId);
        return cat;
    }

    public void addCategory(String catname) throws Exception {
        Category cat = new Category(0, catname);
        CategoriesDB catDB = new CategoriesDB();
        catDB.insertCategory(cat);
    }

    public void updateCategory(int catId, String catName) throws Exception {
        CategoriesDB catDB = new CategoriesDB();
        Category cat = catDB.getCategory(catId);
        cat.setCategoryName(catName);
        catDB.updateCategory(cat);
    }

    public void addItem(String itemname, double price, String useremail, int catID) throws Exception {

        Item item = new Item(0, itemname, price); //create item object
        UserDB userDB = new UserDB();
        User user = userDB.get(useremail); //retrieve user using primary key username
        item.setOwner(user); // set item's owner to user
        CategoriesDB catDB = new CategoriesDB();
        Category cat = catDB.getCategory(catID);//retrieve category using primary key catID
        item.setCategory(cat);// set item's category

        ItemsDB itemDB = new ItemsDB();
        itemDB.insert(item);
    }

    public void updateItem(int itemId, String itemname, double price, int catID) throws Exception {
        ItemsDB itemDB = new ItemsDB();
        Item item = itemDB.get(itemId);
        item.setItemName(itemname);
        item.setPrice(price);

        CategoriesDB catDB = new CategoriesDB();
        Category cat = catDB.getCategory(catID);//retrieve category using primary key catID
        item.setCategory(cat);// set item's category

        itemDB.update(item);
    }

    public Item getItem(int itemID) throws Exception {
        ItemsDB itemDB = new ItemsDB();
        Item item = itemDB.get(itemID);
        return item;
    }

    public void deleteItem(int itemID) throws Exception {
        ItemsDB itemDB = new ItemsDB();
        Item item = itemDB.get(itemID);
        itemDB.delete(item);
    }

    public boolean isTheOwner(String email, int itemID) throws Exception {
        ItemsDB itemDB = new ItemsDB();
        Item item = itemDB.get(itemID);
        if (item.getOwner().getEmail().equals(email)) { //check if the item belongs to the user
            return true;
        }
        return false;
    }

    public int getNumberOfItems(User user) throws Exception {
        ItemsDB itemDB = new ItemsDB();
        String email = user.getEmail();
        int numberOfItems = itemDB.getNumberOfItems(email);
        return numberOfItems;
    }

    public double getValueOfItems(User user) throws Exception {
        ItemsDB itemDB = new ItemsDB();
        String email = user.getEmail();
        double valueOfItems = itemDB.getValueOfItems(email);
        return valueOfItems;
    }

}
