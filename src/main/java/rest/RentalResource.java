package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HouseDTO;
import dtos.RentalDTO;
import facades.RentalFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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


    }
