package facades;

import dtos.ArtPiece.ArtPieceDTO;
import dtos.ArtPiece.ArtPiecesDTO;
import entities.ArtPiece;
import entities.Gallery;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.util.List;

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

    public ArtPieceDTO submitArtPiece(ArtPieceDTO artPieceDTO) {
        EntityManager em = emf.createEntityManager();
        ArtPiece artPiece = new ArtPiece(artPieceDTO.getYear(), artPieceDTO.getName(), artPieceDTO.getArtist(), artPieceDTO.getImageUrl());
        try {
            em.getTransaction().begin();
            em.persist(artPiece);
            em.getTransaction().commit();
            return new ArtPieceDTO(artPiece);
        } finally {
            em.close();
        }
    }

    public ArtPieceDTO editArtPiece(ArtPieceDTO artPieceDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            ArtPiece artPiece = em.find(ArtPiece.class, artPieceDTO.getId());

            artPiece.setImageUrl(artPieceDTO.getImageUrl());
            artPiece.setName(artPieceDTO.getName());

            em.getTransaction().begin();
            em.merge(artPiece);
            em.getTransaction().commit();
            return new ArtPieceDTO(artPiece);
        } finally {
            em.close();
        }
    }

    public ArtPieceDTO deleteArtPiece(int id) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        ArtPiece artPiece = em.find(ArtPiece.class, id);
        if (artPiece == null) {
            throw new WebApplicationException("There's no piece matching the id");
        } else {
            try {
                em.getTransaction().begin();
                em.remove(artPiece);
                em.getTransaction().commit();
                return new ArtPieceDTO(artPiece);
            } finally {
                em.close();
            }
        }
    }

    public ArtPieceDTO getById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            ArtPiece artPiece = em.find(ArtPiece.class, id);
            return new ArtPieceDTO(artPiece);
        } finally {
            em.close();
        }
    }

    public ArtPieceDTO getByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ArtPiece> query = em.createQuery("SELECT a from ArtPiece a WHERE a.name =:name", ArtPiece.class);
            query.setParameter("name", name);
            ArtPiece artPiece = query.getSingleResult();
            return new ArtPieceDTO(artPiece);
        } finally {
            em.close();
        }
    }

    public ArtPiecesDTO getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ArtPiece> query = em.createQuery("SELECT a from ArtPiece a", ArtPiece.class);
            List<ArtPiece> result = query.getResultList();
            return new ArtPiecesDTO(result);
        } finally {
            em.close();
        }
    }

    public Long getCount() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(a.id) FROM ArtPiece a", Long.class);
            long rows = query.getSingleResult();
            return rows;
        } finally {
            em.close();
        }
    }
}
