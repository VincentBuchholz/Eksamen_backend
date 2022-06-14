package facades;

import entities.House;
import entities.Rental;
import entities.Tenant;
import entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class HouseFacadeTest {

    private static EntityManagerFactory emf;
    private static HouseFacade facade;

    House house1;
    House house2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = HouseFacade.getFacadeExample(emf);
    }

    @BeforeEach
    void setUp() {
        house1 = new House("Buddingevej 60","Kgs. Lyngby",7);
        house2 = new House("Agnetevej 10","Holte",5);

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        em.createNamedQuery("House.deleteAllRows").executeUpdate();
        em.getTransaction().commit();

        try{
            em.getTransaction().begin();
            em.persist(house1);
            em.persist(house2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllHousesTest(){
        assertEquals(2,facade.getAllHouses().size());
    }
}