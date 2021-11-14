package rest;

import dtos.ArtPiece.ArtPieceDTO;
import dtos.ArtPiece.ArtPiecesDTO;
import entities.ArtPiece;
import entities.Gallery;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;

import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;

import io.restassured.path.json.JsonPath;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

class ArtResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static ArtPiece a1, a2, a3, a4;
    private static Gallery g1, g2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
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
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        a1 = new ArtPiece(2021, "Mona Lisa", "Leonardo DaVinci", "www.monalisa.com");
        a2 = new ArtPiece(2021, "The Last Supper", "Leonardo DaVinci", "www.thelastsupper.com");
        a3 = new ArtPiece(2021, "The Starry Night", "Vincent van Gogh", "www.thestarrynight.com");
        a4 = new ArtPiece(2021, "Stick Figure", "Me in 3rd grade", "www.blastfromthepast.com");


        g1 = new Gallery("Louvre");
        g2 = new Gallery("Frederik's Apartment");

        g1.addArtPiece(a1);
        g2.addArtPiece(a2);
        g2.addArtPiece(a3);

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        User user = new User("user", "test");
        user.addRole(userRole);
        User admin = new User("admin", "test");
        admin.addRole(adminRole);
        User both = new User("user_admin", "test");
        both.addRole(userRole);
        both.addRole(adminRole);

        try {
            em.getTransaction().begin();
            em.createNamedQuery("ArtPiece.deleteAllRows").executeUpdate();
            em.createNamedQuery("Gallery.deleteAllRows").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.persist(g1);
            em.persist(g2);
            em.persist(a4);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
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

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/art/count").then().statusCode(200);
    }

    @Test
    public void testCount() {
        given()
                .contentType("application/json")
                .get("/art/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body(equalTo("4"));
    }

    @Test
    public void testGetAll() {
        List<ArtPieceDTO> artCollection;
        login("admin", "test");
        artCollection = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .get("/art/all").then()
                .extract()
                .body()
                .jsonPath()
                .getList("artCollection", ArtPieceDTO.class);

        ArtPieceDTO a1DTO = new ArtPieceDTO(a1);
        ArtPieceDTO a2DTO = new ArtPieceDTO(a2);
        ArtPieceDTO a3DTO = new ArtPieceDTO(a3);
        ArtPieceDTO a4DTO = new ArtPieceDTO(a4);

        assertThat(artCollection, containsInAnyOrder(a1DTO, a2DTO, a3DTO, a4DTO));
    }

    @Test
    public void testGetById() {
        given()
                .contentType("application/json")
                .pathParam("id", a1.getId())
                .when()
                .get("art/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo(a1.getName()))
                .body("artist", equalTo(a1.getArtist()));
    }

    @Test
    public void testAddArtPiece() {
        given()
                .contentType("application/json")
                .body(new ArtPieceDTO(2021, "Flower in Vase", "Picasso", "www.interflora.dk"))
                .when()
                .post("art")
                .then()
                .body("id", notNullValue())
                .body("year", equalTo(2021))
                .body("name", equalTo("Flower in Vase"))
                .body("artist", equalTo("Picasso"));

    }

    @Test
    public void testEdit() {
        ArtPieceDTO artPiece = new ArtPieceDTO(a1);
        artPiece.setName("Lisa Mona");
        artPiece.setImageUrl("www.cheapknockoff.com");

        given()
                .contentType("application/json")
                .pathParam("id", a1.getId())
                .body(artPiece)
                .when()
                .put("art/{id}")
                .then()
                .statusCode(200);
        //.body("name", equalTo("Lisa Mona"))
        //.body("artist", equalTo("Leonardo DaVinci"))
        //.body("imageUrl", equalTo("www.cheapknockoff.com"));
    }

    @Test
    public void testDelete() {
        given()
                .contentType("application/json")
                .pathParam("id", a4.getId())
                .delete("art/{id}")
                .then()
                .assertThat()
                .statusCode(200);

        List<ArtPieceDTO> artCollection;

        artCollection = given()
                .contentType("application/json")
                .when()
                .get("/art/all")
                .then()
                .extract().body().jsonPath().getList("artCollection", ArtPieceDTO.class);

        ArtPieceDTO p4DTO = new ArtPieceDTO(a4);

        assertThat(artCollection, not(hasItem(p4DTO)));
    }

}
