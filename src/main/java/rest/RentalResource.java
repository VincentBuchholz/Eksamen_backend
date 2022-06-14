package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HouseDTO;
import dtos.RentalDTO;
import entities.Rental;
import facades.RentalFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rental")
    public class RentalResource {

        private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

        private static final RentalFacade FACADE =  RentalFacade.getFacadeExample(EMF);
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Path("/rentals/{userID}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRentalsByUserID(@PathParam("userID") int userID) {
        List<RentalDTO> rentalDTOS = FACADE.getRentalsByUserID(userID);
        return Response.ok().entity(GSON.toJson(rentalDTOS)).build();
    }

    @GET
    @Path("/house/{rentalID}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getHouseByRentalID(@PathParam("rentalID") int rentalID) {
        HouseDTO houseDTO = FACADE.getHouseByRentalID(rentalID);
        return Response.ok().entity(GSON.toJson(houseDTO)).build();
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllRentals() {
        List<RentalDTO> rentalDTOS = FACADE.getAllRentals();
        return Response.ok().entity(GSON.toJson(rentalDTOS)).build();
    }

    @GET
    @Path("/{rentalID}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllRentals(@PathParam("rentalID") int rentalID) {
        RentalDTO rentalDTO = FACADE.getRentalByID(rentalID);
        return Response.ok().entity(GSON.toJson(rentalDTO)).build();
    }

    @PUT
    @Path("/updateinfo")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response updateRentalInfo(String content){
        RentalDTO rentalDTO = GSON.fromJson(content, RentalDTO.class);
        RentalDTO newRentalDTO = FACADE.updateRentalInfo(rentalDTO);
        return Response.ok().entity(GSON.toJson(newRentalDTO)).build();
    }



    }
