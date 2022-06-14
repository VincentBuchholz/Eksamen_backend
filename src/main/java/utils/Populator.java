package utils;

import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Populator {
    public static void POPULATE(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        User admin = new User("admin","test123");
        admin.setRole(adminRole);
        em.persist(userRole);
        em.persist(adminRole);
        em.persist(admin);
        em.getTransaction().commit();
    }
}
