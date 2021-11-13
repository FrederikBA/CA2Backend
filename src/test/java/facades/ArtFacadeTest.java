package facades;

import dtos.ArtPiece.ArtPieceDTO;
import dtos.Gallery.GalleryDTO;
import entities.ArtPiece;
import entities.Gallery;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;

import static org.junit.jupiter.api.Assertions.*;

class ArtFacadeTest {
    private static EntityManagerFactory emf;
    private static ArtFacade facade;
    private static GalleryFacade galleryFacade;
    private static ArtPiece a1, a2, a3, a4;
    private static Gallery g1, g2;

    public ArtFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ArtFacade.getArtFacade(emf);
        galleryFacade = GalleryFacade.getGalleryFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
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

        try {
            em.getTransaction().begin();
            em.createNamedQuery("ArtPiece.deleteAllRows").executeUpdate();
            em.createNamedQuery("Gallery.deleteAllRows").executeUpdate();
            em.persist(g1);
            em.persist(g2);
            em.persist(a4);
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
        long expected = 4;
        long actual = facade.getCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetById() {
        int expected = a1.getId();
        int actual = facade.getById(a1.getId()).getId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAll() {
        int expected = 4;
        int actual = facade.getAll().getSize();
        assertEquals(expected, actual);

        ArtPieceDTO a1DTO = new ArtPieceDTO(a1);
        ArtPieceDTO a2DTO = new ArtPieceDTO(a2);
        ArtPieceDTO a3DTO = new ArtPieceDTO(a3);
        ArtPieceDTO a4DTO = new ArtPieceDTO(a4);

        assertThat(facade.getAll().getArtCollection(), containsInAnyOrder(a1DTO, a2DTO, a3DTO, a4DTO));
    }

    @Test
    public void testSubmitArtPiece() {
        ArtPieceDTO a5DTO = facade.submitArtPiece(new ArtPieceDTO(2003, "Hus med flot Sol", "Frederik", "www.frederikcphb.dk"));

        long expected = 5;
        long actual = facade.getCount();
        assertEquals(expected, actual);

        ArtPieceDTO a1DTO = new ArtPieceDTO(a1);
        ArtPieceDTO a2DTO = new ArtPieceDTO(a2);
        ArtPieceDTO a3DTO = new ArtPieceDTO(a3);
        ArtPieceDTO a4DTO = new ArtPieceDTO(a4);


        assertThat(facade.getAll().getArtCollection(), containsInAnyOrder(a1DTO, a2DTO, a3DTO, a4DTO, a5DTO));
    }

    @Test
    public void testEditArtPiece() {
        a1.setImageUrl("www.louvre.fr/monalisa");
        a1.setName("Leonardo DiCaprio");

        ArtPieceDTO editedPiece = new ArtPieceDTO(a1);
        facade.editArtPiece(editedPiece);

        assertEquals(facade.getById(a1.getId()).getImageUrl(), editedPiece.getImageUrl());
        assertEquals(facade.getById(a1.getId()).getName(), editedPiece.getName());
    }

    @Test
    public void testDeleteArtPiece() {
        facade.deleteArtPiece(a3.getId());

        assertEquals(3, facade.getCount());

        ArtPieceDTO a1DTO = new ArtPieceDTO(a1);
        ArtPieceDTO a2DTO = new ArtPieceDTO(a2);
        ArtPieceDTO a3DTO = new ArtPieceDTO(a3);
        ArtPieceDTO a4DTO = new ArtPieceDTO(a4);

        //Confirm that the piece is now missing form the Art Collection
        assertThat(facade.getAll().getArtCollection(), not(hasItem(a3DTO)));

        //Confirm that the other pieces are still in the Art Collection
        assertThat(facade.getAll().getArtCollection(), hasItem(a1DTO));
        assertThat(facade.getAll().getArtCollection(), hasItem(a2DTO));
        assertThat(facade.getAll().getArtCollection(), hasItem(a4DTO));

    }
}
