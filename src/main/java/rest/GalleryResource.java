package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ArtPiece.ArtPieceDTO;
import dtos.Gallery.GalleryDTO;
import facades.GalleryFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("/gallery")
public class GalleryResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final GalleryFacade facade = GalleryFacade.getGalleryFacade(EMF);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") int id) {
        return gson.toJson(facade.getById(id));
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll() {
        return gson.toJson(facade.getAll());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addGallery(String gallery) {
        GalleryDTO g = gson.fromJson(gallery, GalleryDTO.class);
        GalleryDTO gNew = facade.addGallery(g);
        return gson.toJson(gNew);
    }

    @Path("add/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addToGallery(@PathParam("id") int id, String artPiece) {
        ArtPieceDTO a = gson.fromJson(artPiece, ArtPieceDTO.class);
        GalleryDTO gEdited = facade.addToGallery(id, a);

        return gson.toJson(gEdited);
    }

    @Path("/{id}")
    @PUT
    public String editGallery(@PathParam("id") int id, String gallery) {
        GalleryDTO g = gson.fromJson(gallery, GalleryDTO.class);
        g.setId(id);
        GalleryDTO gEdited = facade.editGallery(g);
        return gson.toJson(gEdited);
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteGallery(@PathParam("id") int id) {
        GalleryDTO gDeleted = facade.deleteGallery(id);
        return gson.toJson(gDeleted);
    }
}