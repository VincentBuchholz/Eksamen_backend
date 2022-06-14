package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HouseDTO;
import facades.HouseFacade;
import facades.RentalFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/house")
public class HouseResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final HouseFacade FACADE =  HouseFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllHouses() {
        List<HouseDTO> houseDTOS = FACADE.getAllHouses();
        return Response.ok().entity(GSON.toJson(houseDTOS)).build();
    }

}
