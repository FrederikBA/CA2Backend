package facades;

import dtos.ArtPiece.ArtPieceDTO;
import dtos.Gallery.GalleriesDTO;
import dtos.Gallery.GalleryDTO;
import entities.ArtPiece;
import entities.Gallery;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.util.List;

public class GalleryFacade {
    private static EntityManagerFactory emf;
    private static GalleryFacade instance;

    private GalleryFacade() {
    }

    public static GalleryFacade getGalleryFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GalleryFacade();
        }
        return instance;
    }

    public GalleryDTO addGallery(GalleryDTO galleryDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Gallery gallery = new Gallery(galleryDTO.getGalleryName());
            em.getTransaction().begin();
            em.persist(gallery);
            em.getTransaction().commit();
            return new GalleryDTO(gallery);
        } finally {
            em.close();
        }
    }

    public String addToGallery(int galleryId, int artPieceId) {
        EntityManager em = emf.createEntityManager();
        try {
            Gallery gallery = em.find(Gallery.class, galleryId);
            ArtPiece artPiece = em.find(ArtPiece.class, artPieceId);
            gallery.addArtPiece(artPiece);

            em.getTransaction().begin();
            em.merge(gallery);
            em.getTransaction().commit();

            return artPiece.getName() + " was added to: " + gallery.getGalleryName();
        } finally {
            em.close();
        }
    }

    public GalleryDTO editGallery(GalleryDTO galleryDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Gallery gallery = em.find(Gallery.class, galleryDTO.getId());
            gallery.setGalleryName(galleryDTO.getGalleryName());

            em.getTransaction().begin();
            em.merge(gallery);
            em.getTransaction().commit();

            return new GalleryDTO(gallery);
        } finally {
            em.close();
        }
    }

    public GalleryDTO deleteGallery(int id) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        Gallery gallery = em.find(Gallery.class, id);
        if (gallery == null) {
            throw new WebApplicationException("There's no gallery matching the id");
        } else {
            try {
                return null;
            } finally {
                em.close();
            }
        }
    }

    public GalleryDTO getById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Gallery gallery = em.find(Gallery.class, id);
            return new GalleryDTO(gallery);
        } finally {
            em.close();
        }
    }

    public GalleriesDTO getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Gallery> query = em.createQuery("SELECT g from Gallery g", Gallery.class);
            List<Gallery> result = query.getResultList();
            return new GalleriesDTO(result);
        } finally {
            em.close();
        }
    }
}
