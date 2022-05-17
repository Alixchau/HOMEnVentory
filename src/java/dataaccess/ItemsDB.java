package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import models.Category;
import models.Item;
import models.User;

/**
 *
 * @author Alix
 */
public class ItemsDB {

    public Collection<Item> getAll(String email) throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            User user = em.find(User.class, email);
            Collection<Item> items = user.getItemCollection();
            return items;
        } finally {
            em.close();
        }
    }

    //for excel export function, get the number of items a user own
    public int getNumberOfItems(String email) throws Exception {
        ConnectionPool cp = ConnectionPool.getInstance();
        Connection con = cp.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM item WHERE owner=?";
        int numberOfItems = 0;

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                numberOfItems = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            cp.freeConnection(con);
        }
        return numberOfItems;
    }

    //for excel export function, get the total value of items a user own
    public double getValueOfItems(String email) throws Exception {
        ConnectionPool cp = ConnectionPool.getInstance();
        Connection con = cp.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT SUM(price) FROM item WHERE owner=?";
        double valueOfItems = 0;

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                valueOfItems = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            cp.freeConnection(con);
        }
        return valueOfItems;
    }

    public Collection<Item> getSearchItems(String searchName) throws Exception {
        ConnectionPool cp = ConnectionPool.getInstance();
        Connection con = cp.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection<Item> searchItems = new ArrayList<>();
        Item item = null;
        String sql = "SELECT * FROM item WHERE item_name LIKE ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchName + "%"); //% for wildcard
            rs = ps.executeQuery();
            while (rs.next()) {
                int item_id = rs.getInt(1);
                String item_name = rs.getString(3);
                double item_price = rs.getDouble(4);
                String owner_email = rs.getString(5);
                UserDB userdb = new UserDB();
                User user = userdb.get(owner_email);
                item = new Item(item_id, item_name, item_price);
                item.setOwner(user);
                searchItems.add(item);
            }
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            cp.freeConnection(con);
        }
        return searchItems;
    }

    public Item get(int itemId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            Item item = em.find(Item.class, itemId);
            return item;
        } finally {
            em.close();
        }
    }

    public void insert(Item item) throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();

            Category cat = item.getCategory(); //get the category that the item belongs to
            cat.getItemCollection().add(item); //add the item to the items list of the category

            User user = item.getOwner(); //get the owner of the item
            user.getItemCollection().add(item); //add the item to the items list of the user. Many items belong to one user, update the many side data 

            em.persist(item);
            em.merge(cat);
            em.merge(user);
            trans.commit();

        } catch (Exception ex) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public void update(Item item) throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(item);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public void delete(Item item) throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Category cat = item.getCategory();
            cat.getItemCollection().remove(item);

            User user = item.getOwner();
            user.getItemCollection().remove(item);

            em.remove(em.merge(item));
            em.merge(cat);
            em.merge(user);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        } finally {
            em.close();
        }
    }
}
