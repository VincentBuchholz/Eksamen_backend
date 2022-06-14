package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HouseDTO;
import dtos.RentalDTO;
import entities.*;
import facades.RentalFacade;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import rest.ApplicationConfig;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class RentalEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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
    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    private static String securityToken;

    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }


    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        house1 = new House("testAddress1", "testCity1", 1, "img");
        house2 = new House("testAddress2", "testCity2", 1, "img");


        house1 = new House("Buddingevej 60","Kgs. Lyngby",7,"img_url");
        house2 = new House("Agnetevej 10","Holte",5,"img_url");
        tenant1 = new Tenant("John Larsen","+45224422","Håndværker");
        tenant2 = new Tenant("Erik Hansen","+45224422","Developer");
        tenant3 = new Tenant("Sofie Larsen","+45224422","HR");
        rental1 = new Rental("12/06/2022","20/07/2022",150000,15000,"Lars");
        rental2 = new Rental("21/7/2022","21/09/2022",150000,15000,"Lars");
        rental3 = new Rental("20/08/2022","20/10/2022",120000,12000,"Lars");
        user1 = new User("user1","test123");
        user2 = new User("user2","test123");
        user3 = new User("user3","test123");
//        tenant1.setUser(user1);
//        tenant2.setUser(user2);
//        tenant3.setUser(user3); add back to test locally

        house1.addRental(rental1);
        house1.addRental(rental2);
        house2.addRental(rental3);
        rental1.addTenant(tenant1);
        rental2.addTenant(tenant2);
        rental3.addTenant(tenant3);
        rental3.addTenant(tenant1);

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
//            em.persist(user1);
//            em.persist(user2);
//            em.persist(user3); add back to test locally
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        EntityManager em = emf.createEntityManager();

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        User user = new User("user", "test");
        user.setRole(userRole);
        User admin = new User("admin", "test");
        em.getTransaction().begin();
        admin.setRole(adminRole);
        em.persist(userRole);
        em.persist(adminRole);
        em.persist(user);
        em.persist(admin);
        //System.out.println("Saved test data to database");
        em.getTransaction().commit();
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    @Test
    @Disabled
    void getRentalsByUserIDTest(){
        login("user","test");


        List<RentalDTO> rentalDTOS;

        rentalDTOS = given()
                .header("Content-type", ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .when()
                .get("rental/rentals/"+user1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("",RentalDTO.class);

        RentalDTO rentalDTO1 = new RentalDTO(rental1);
        RentalDTO rentalDTO2 = new RentalDTO(rental3);

        assertThat(rentalDTOS,containsInAnyOrder(rentalDTO1,rentalDTO2));
    }

    @Test
    void getHouseByRentalIDAsAdminTest(){
        login("admin", "test");

        HouseDTO houseDTOGot;

        houseDTOGot = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/rental/house/"+rental1.getId()).then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("",HouseDTO.class);
        HouseDTO houseDTO = new HouseDTO(house1);

        assertThat(houseDTO,equalTo(houseDTOGot));
    }

    @Test
    void getHouseByRentalIDAsUserTest(){
        login("user", "test");

        HouseDTO houseDTOGot;

        houseDTOGot = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/rental/house/"+rental1.getId()).then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("",HouseDTO.class);
        HouseDTO houseDTO = new HouseDTO(house1);

        assertThat(houseDTO,equalTo(houseDTOGot));
    }

    @Test
    void getAllRentalsTest(){
        login("admin","test");


        List<RentalDTO> rentalDTOS;

        rentalDTOS = given()
                .header("Content-type", ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .when()
                .get("rental/all")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("",RentalDTO.class);

        RentalDTO rentalDTO1 = new RentalDTO(rental1);
        RentalDTO rentalDTO2 = new RentalDTO(rental2);
        RentalDTO rentalDTO3 = new RentalDTO(rental3);

        assertThat(rentalDTOS,containsInAnyOrder(rentalDTO1,rentalDTO2,rentalDTO3));
    }

    @Test
    @Disabled
    void getRentalByIDAsAdminTest(){
        login("admin", "test");

        RentalDTO rentalDTOGot;

        rentalDTOGot = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/rental/"+rental1.getId()).then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("",RentalDTO.class);
        RentalDTO rentalDTO = new RentalDTO(rental1);

        assertThat(rentalDTO,equalTo(rentalDTOGot));
    }

    @Test
    @Disabled
    void getRentalByIDAsUserTest(){
        login("user", "test");

        RentalDTO rentalDTOGot;

        rentalDTOGot = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/rental/"+rental1.getId()).then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("",RentalDTO.class);
        RentalDTO rentalDTO = new RentalDTO(rental1);

        assertThat(rentalDTO,equalTo(rentalDTOGot));
    }

    @Test
    void updateRentalInfoTest(){

        RentalDTO rentalDTO = new RentalDTO(rental1);
        rentalDTO.setStart("01/01/2023");
        rentalDTO.setEnd("01/01/2024");
        rentalDTO.setContact("testcontact");
        String requestBody = GSON.toJson(rentalDTO);

        login("admin","test");


        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(requestBody)
                .when()
                .put("/rental/updateinfo")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(rental1.getId()))
                .body("start", equalTo(rentalDTO.getStart()))
                .body("end", equalTo(rentalDTO.getEnd()))
                .body("contact",equalTo(rentalDTO.getContact()));
    }

    @Test
    void changeHouseTest(){
        login("admin","test");
        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .when()
                .put("/rental/changehouse/"+rental3.getId()+"/"+house1.getId())
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    void deleteRentalByIDTest(){
        login("admin", "test");
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .delete("/rental/delete/{id}", rental3.getId())
                .then()
                .statusCode(200)
                .body("id",equalTo(rental3.getId()));

    }

    @Test
    void createRentalTest(){
        login("admin","test");

        RentalDTO rentalDTO = new RentalDTO("01/01/2020","01/01/2023",15000,1500,"Lars", house1.getId(), tenant1.getId());
        String requestBody = GSON.toJson(rentalDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(requestBody)
                .when()
                .post("/rental/create")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue());
    }






}