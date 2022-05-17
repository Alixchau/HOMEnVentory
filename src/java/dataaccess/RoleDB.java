package dataaccess;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import models.Role;

public class RoleDB {

    public Collection<Role> getAll() throws Exception {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            Collection<Role> roles = em.createNamedQuery("Role.findAll", Role.class).getResultList();
            return roles;
        } finally {
            em.close();
        }
    }

    public Role get(int role_id) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            Role role = em.find(Role.class, role_id);
            return role;
        } finally {
            em.close();
        }
    }

}
