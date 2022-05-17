package models;

/**
 *
 * @author Alix
 */
public class UserInventory {

    private User user;
    private int numberOfItems;
    private double valueOfItems;

    public UserInventory(User user, int numberOfItems, double valueOfItems) {
        this.user = user;
        this.numberOfItems = numberOfItems;
        this.valueOfItems = valueOfItems;
    }

    public User getUser() {
        return user;
    }
    
    public int getNumberOfItems(){
        return numberOfItems;        
    }
    
    public double getValueOfItems(){
        return valueOfItems;
    }
}
