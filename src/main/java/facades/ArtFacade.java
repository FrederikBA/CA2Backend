package facades;

import javax.persistence.EntityManagerFactory;

public class ArtFacade {
    private static EntityManagerFactory emf;
    private static ArtFacade instance;

    private ArtFacade() {
    }

    public static ArtFacade getArtFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ArtFacade();
        }
        return instance;
    }
}
