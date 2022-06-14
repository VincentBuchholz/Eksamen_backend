package facades;

import dtos.TenantDTO;
import entities.House;
import entities.Rental;
import entities.Tenant;
import entities.User;
import errorhandling.UsernameTakenException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static facades.UserFacade.usernameTaken;
import static org.junit.jupiter.api.Assertions.*;

class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;

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

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }

    @BeforeEach
    void setUp() {
        house1 = new House("Buddingevej 60","Kgs. Lyngby",7,"img url");
        house2 = new House("Agnetevej 10","Holte",5,"img url");
        tenant1 = new Tenant("John Larsen","+45224422","Håndværker");
        tenant2 = new Tenant("Erik Hansen","+45224422","Developer");
        tenant3 = new Tenant("Sofie Larsen","+45224422","HR");
        rental1 = new Rental("20/06/2022","20/07/2022",150000,15000,"Lars");
        rental2 = new Rental("21/7/2022","21/09/2022",150000,15000,"Lars");
        rental3 = new Rental("20/08/2022","/10/2022",120000,12000,"Lars");
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

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

//        em.createNamedQuery("Tenant.deleteAllRows").executeUpdate();
//        em.createNamedQuery("Role.deleteAllRows").executeUpdate();
//        em.createNamedQuery("User.deleteAllRows").executeUpdate();
        em.createNamedQuery("Rental.deleteAllRows").executeUpdate();
        em.createNamedQuery("House.deleteAllRows").executeUpdate();
        em.getTransaction().commit();

        try{
            em.getTransaction().begin();
            em.persist(house1);
            em.persist(house2);
            em.persist(rental1);
            em.persist(rental2);
            em.persist(rental3);
            em.persist(tenant1);
            em.persist(tenant2);
            em.persist(tenant3);
            em.persist(user1);
            em.persist(user2);
            em.persist(user3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void CreateUserTest() throws UsernameTakenException {
        System.out.println("create user test");
        TenantDTO tenantToCreate = new TenantDTO("Testname","22222222","testJob","testusername","testpass");
        assertNotNull(facade.createUser(tenantToCreate).getId());
    }

    @Test
    @Disabled
    void getAllTenants(){
        System.out.println("Get all tenants test");
        assertEquals(3,facade.getAllTenants().size());
    }

    @Test
    void getTenatsByRentalIDTest(){
        System.out.println("get tenants by rental ID");
        assertEquals(2,facade.getTenantsByRentalID(rental3.getId()).size());
    }

    @Test
    void userNameIsTakenTest(){
        System.out.println("userName is taken test!");
        assertTrue(usernameTaken(user1.getUserName()));
        assertFalse(usernameTaken("notTaken"));
    }
}