/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import entities.ArtPiece;
import entities.Gallery;
import utils.EMF_Creator;

public class Populator {
    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        Gallery g1 = new Gallery("TestGallery");
        ArtPiece a1 = new ArtPiece(2021, "TestPainting", "Leonardo DiCaprio", "themonalisa.dk");
        g1.addArtPiece(a1);

        em.getTransaction().begin();
        em.persist(a1);
        em.persist(g1);
        em.getTransaction().commit();
    }

    public static void main(String[] args) {
        populate();
    }
}
