package facades;

import javax.persistence.EntityManagerFactory;

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



}
