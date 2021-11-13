/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import dtos.ArtPiece.ArtPieceDTO;
import dtos.Gallery.GalleryDTO;
import entities.ArtPiece;
import entities.Gallery;
import utils.EMF_Creator;

public class Populator {
    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        ArtFacade artFacade = ArtFacade.getArtFacade(emf);
        GalleryFacade galleryFacade = GalleryFacade.getGalleryFacade(emf);

        galleryFacade.addGallery(new GalleryDTO("Louvre"));

        artFacade.submitArtPiece(new ArtPieceDTO(2021, "Mona Lisa", "Leonardo DaVinci", "www.louvre.fr/monalisa"));
    }

    public static void main(String[] args) {
        populate();
    }
}
