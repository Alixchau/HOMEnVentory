package dataaccess;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import models.Category;

/**
 *
 * @author Alix
 */
public class CategoriesDB {

    public Collection<Category> getAll() throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            Collection<Category> categories = em.createNamedQuery("Category.findAll", Category.class).getResultList();
            return categories;
        } finally {
            em.close();
        }

    }

    public Category getCategory(int categoryID) throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            Category cat = em.find(Category.class, categoryID);
            return cat;
        } finally {
            em.close();
        }
    }

    public void updateCategory(Category cat) throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(cat);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        } finally {
            em.close();
        }
    }
    
    public void insertCategory(Category cat) throws Exception{
                EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            trans.begin();
            em.persist(cat);
            trans.commit();
        }catch(Exception ex){
            trans.rollback();
        }finally{
            em.close();
        }
    }

}
