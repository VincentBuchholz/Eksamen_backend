package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HouseDTO;
import dtos.RentalDTO;
import dtos.TenantDTO;
import entities.Rental;
import errorhandling.DateFormatException;
import facades.RentalFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.List;

@Path("/rental")
    public class RentalResource {

        private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

        private static final RentalFacade FACADE =  RentalFacade.getFacadeExample(EMF);
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Path("/rentals/{userID}")
    @RolesAllowed("user")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRentalsByUserID(@PathParam("userID") int userID) {
        List<RentalDTO> rentalDTOS = FACADE.getRentalsByUserID(userID);
        return Response.ok().entity(GSON.toJson(rentalDTOS)).build();
    }

    @GET
    @Path("/house/{rentalID}")
    @RolesAllowed({"admin","user"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getHouseByRentalID(@PathParam("rentalID") int rentalID) {
        HouseDTO houseDTO = FACADE.getHouseByRentalID(rentalID);
        return Response.ok().entity(GSON.toJson(houseDTO)).build();
    }

    @GET
    @Path("/all")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllRentals() {
        List<RentalDTO> rentalDTOS = FACADE.getAllRentals();
        return Response.ok().entity(GSON.toJson(rentalDTOS)).build();
    }

    @GET
    @Path("/{rentalID}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"admin","user"})
    public Response getRentalByID(@PathParam("rentalID") int rentalID) {
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

    @PUT
    @Path("/changehouse/{rentalID}/{houseID}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response setHouse(@PathParam("rentalID") int rentalID,@PathParam("houseID") int houseID){
        RentalDTO newRentalDTO = FACADE.setHouse(rentalID,houseID);
        return Response.ok().entity(GSON.toJson(newRentalDTO)).build();
    }

    @PUT
    @Path("/addtenant/{rentalID}/{tenantID}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response AddTenantToRental(@PathParam("rentalID") int rentalID,@PathParam("tenantID") int tenantID){
        RentalDTO newRentalDTO = FACADE.addTenantToRental(rentalID,tenantID);
        return Response.ok().entity(GSON.toJson(newRentalDTO)).build();
    }

    @PUT
    @Path("/removetenant/{rentalID}/{tenantID}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response removeTenantFromRental(@PathParam("rentalID") int rentalID,@PathParam("tenantID") int tenantID){
        RentalDTO newRentalDTO = FACADE.removeTenantFromRental(rentalID,tenantID);
        return Response.ok().entity(GSON.toJson(newRentalDTO)).build();
    }

    @DELETE
    @Path("/delete/{rentalID}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response deleteRentalByID(@PathParam("rentalID") int rentalID) {
        RentalDTO deleted = FACADE.deleteRental(rentalID);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }

    @GET
    @Path("/currenttenants/{houseID}")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCurrentTenants(@PathParam("houseID") int houseID) throws ParseException {
        List<TenantDTO> tenantDTOs = FACADE.getCurrentTenantsByHouseID(houseID);
        return Response.ok().entity(GSON.toJson(tenantDTOs)).build();
    }

    @GET
    @Path("/rentalshouse/{houseID}")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRentalByHouseID(@PathParam("houseID") int houseID) {
        List<RentalDTO> rentalDTOS = FACADE.getRentalsByHouseID(houseID);
        return Response.ok().entity(GSON.toJson(rentalDTOS)).build();
    }

    @POST
    @Path("/create")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response createRental(String content) throws DateFormatException {
        RentalDTO rentalDTO = GSON.fromJson(content, RentalDTO.class);
        RentalDTO newRentalDTO = FACADE.createRental(rentalDTO);
        return Response.ok().entity(GSON.toJson(newRentalDTO)).build();
    }




    }
