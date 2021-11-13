package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ArtPiece.ArtPieceDTO;
import facades.ArtFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("/art")
public class ArtResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final ArtFacade facade = ArtFacade.getArtFacade(EMF);
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

    @Path("/count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCount() {
        return gson.toJson(facade.getCount());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String submitArtPiece(String artPiece) {
        ArtPieceDTO a = gson.fromJson(artPiece, ArtPieceDTO.class);
        ArtPieceDTO aNew = facade.submitArtPiece(a);
        return gson.toJson(aNew);
    }

    @Path("/{id}")
    @PUT
    public String editArtPiece(@PathParam("id") int id, String artPiece) {
        ArtPieceDTO a = gson.fromJson(artPiece, ArtPieceDTO.class);
        a.setId(id);
        ArtPieceDTO aEdited = facade.editArtPiece(a);
        return gson.toJson(aEdited);
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deletePerson(@PathParam("id") int id) {
        ArtPieceDTO aDeleted = facade.deleteArtPiece(id);
        return gson.toJson(aDeleted);
    }
}