package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HouseDTO;
import entities.House;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

class HouseEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    House house1;
    House house2;

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
        house1 = new House("testAddress1","testCity1",1,"img");
        house2 = new House("testAddress2","testCity2",1,"img");

        em.getTransaction().begin();
        em.createNamedQuery("User.deleteAllRows").executeUpdate();
        em.createNamedQuery("Role.deleteAllRows").executeUpdate();
        em.createNamedQuery("Rental.deleteAllRows").executeUpdate();
        em.createNamedQuery("House.deleteAllRows").executeUpdate();
        em.getTransaction().commit();

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



        try{
            em.getTransaction().begin();
            em.persist(house1);
            em.persist(house2);
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

        EntityManager em = emf.createEntityManager();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }
    @Test
    void GetAllHousesTest(){

        List<HouseDTO> houseDTOS;

        houseDTOS = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/house/all").then()
                .statusCode(200)
                .extract().body().jsonPath().getList("",HouseDTO.class);
        HouseDTO houseDTO1 = new HouseDTO(house1);
        HouseDTO houseDTO2 = new HouseDTO(house2);
        assertThat(houseDTOS,containsInAnyOrder(houseDTO1,houseDTO2));
    }

    @Test
    void createHouseTest(){

        login("admin","test");
        HouseDTO houseDTO = new HouseDTO("testaddress","testcity",8);
        String requestBody = GSON.toJson(houseDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(requestBody)
                .when()
                .post("/house/create")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    void getHouseByIdTest(){
        login("admin","test");



        HouseDTO houseDTOFound = given()
                .header("Content-type", ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .when()
                .get("house/"+house1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getObject("",HouseDTO.class);

        HouseDTO houseDTO = new HouseDTO(house1);

        assertThat(houseDTO,equalTo(houseDTOFound));

    }
    }
