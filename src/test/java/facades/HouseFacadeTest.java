package facades;

import dtos.HouseDTO;
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
        house1 = new House("Buddingevej 60","Kgs. Lyngby",7,"img url");
        house2 = new House("Agnetevej 10","Holte",5,"img url");

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
        System.out.println("Get all houses test!");
        assertEquals(2,facade.getAllHouses().size());
    }

    @Test
    void createHouseTest(){
        System.out.println("create house test!");
        HouseDTO houseToCreate = new HouseDTO("test address","testcity",12);
        HouseDTO houseCreated = facade.createHouse(houseToCreate);
        assertNotNull(houseCreated.getId());
    }

    @Test
    void getHouseByIDTest(){
        System.out.println("Get house by ID test!");
        assertEquals(new HouseDTO(house1),facade.getHouseByID(house1.getId()));
    }


}