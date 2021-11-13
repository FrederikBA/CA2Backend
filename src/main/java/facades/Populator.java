/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import dtos.ArtPiece.ArtPieceDTO;
import entities.ArtPiece;
import entities.Gallery;
import utils.EMF_Creator;

public class Populator {
    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        ArtFacade facade = ArtFacade.getArtFacade(emf);
    }

    public static void main(String[] args) {
        populate();
    }
}
