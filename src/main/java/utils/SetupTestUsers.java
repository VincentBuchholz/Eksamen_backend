package utils;


import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

  public static void main(String[] args) {

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();

    House house1;
    House house2;
    Tenant tenant1;
    Tenant tenant2;
    Tenant tenant3;
    Rental rental1;
    Rental rental2;
    Rental rental3;
    User user1;
    User user2;
    User user3;

    house1 = new House("St. antoni 15","Barcelona",7,"https://barcelona-home.com/blog/wp-content/upload/2013/10/villafeat-955x508.jpg");
    house2 = new House("Montréal road 17 ","Nice",5,"https://www.cvvillas.com/dynamic-images/17000-17999/17358/17358_c=(0,1,2000,1333)_w=1366_h=911_pjpg.jpg?v=55e08f31cac4563c31f48812b4aa4b6017ba78c3");
    tenant1 = new Tenant("John Larsen","+45224422","Håndværker");
    tenant2 = new Tenant("Erik Hansen","+45224422","Developer");
    tenant3 = new Tenant("Sofie Larsen","+45224422","HR");
    rental1 = new Rental("20/06/2022","20/07/2022",150000,15000,"Lars");
    rental2 = new Rental("21/7/2022","21/09/2022",150000,15000,"Lars");
    rental3 = new Rental("20/08/2022","20/10/2022",120000,12000,"Lars");
    user1 = new User("user1","test123");
    user2 = new User("user2","test123");
    user3 = new User("user3","test123");
    tenant1.setUser(user1);
    tenant2.setUser(user2);
    tenant3.setUser(user3);

    house1.addRental(rental1);
    house1.addRental(rental2);
    house2.addRental(rental3);
    rental1.addTenant(tenant1);
    rental2.addTenant(tenant2);
    rental3.addTenant(tenant3);
    rental3.addTenant(tenant1);
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords


    User admin = new User("admin", "test123");

    if(admin.getUserPass().equals("test")||user1.getUserPass().equals("test"))
      throw new UnsupportedOperationException("You have not changed the passwords");

    em.getTransaction().begin();
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
    user1.setRole(userRole);
    user2.setRole(userRole);
    user3.setRole(userRole);
    admin.setRole(adminRole);
    em.persist(userRole);
    em.persist(adminRole);
    em.persist(user1);
    em.persist(user2);
    em.persist(user3);
    em.persist(admin);
    em.persist(house1);
    em.persist(house2);
    em.persist(rental1);
    em.persist(rental2);
    em.persist(rental3);
    em.persist(tenant1);
    em.persist(tenant2);
    em.persist(tenant3);
    em.getTransaction().commit();
    System.out.println("PW: " + user1.getUserPass());
    System.out.println("Testing user with OK password: " + user1.verifyPassword("test"));
    System.out.println("Testing user with wrong password: " + user1.verifyPassword("test1"));
    System.out.println("Created TEST Users");
   
  }

}
