package facades;

import entities.ArtPiece;
import entities.Gallery;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class ArtFacadeTest {
    private static EntityManagerFactory emf;
    private static ArtFacade facade;
    private static ArtPiece a1, a2, a3;
    private static Gallery g1, g2;

    public ArtFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ArtFacade.getArtFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        //Make objects
        //int year, String name, String artist, String imageUrl

        a1 = new ArtPiece(2021, "Mona Lisa", "Leonardo DaVinci", "www.monalisa.com");
        a2 = new ArtPiece(2021, "The Last Supper", "Leonardo DaVinci", "www.thelastsupper.com");
        a3 = new ArtPiece(2021, "The Starry Night", "Vincent van Gogh", "www.thestarrynight.com");

        g1 = new Gallery("Louvre");
        g2 = new Gallery("Frederik's Apartment");

        g1.addArtPiece(a1);
        g2.addArtPiece(a2);
        g2.addArtPiece(a3);

        try {
            em.getTransaction().begin();
            em.createNamedQuery("ArtPiece.deleteAllRows").executeUpdate();
            em.createNamedQuery("Gallery.deleteAllRows").executeUpdate();
            em.persist(g1);
            em.persist(g2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testCount() {
        long expected = 3;
        long actual = facade.getCount();
        assertEquals(expected, actual);
    }
}
