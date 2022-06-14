package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.TenantDTO;

import errorhandling.UsernameTakenException;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
public class UserResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final UserFacade FACADE =  UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response createUser(String content) throws UsernameTakenException {
        TenantDTO tenantDTO = GSON.fromJson(content, TenantDTO.class);
        System.out.println(tenantDTO);
        TenantDTO newTenantDTO = FACADE.createUser(tenantDTO);
        return Response.ok().entity(GSON.toJson(newTenantDTO)).build();
    }

    @GET
    @Path("/all")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllTenants() {
        List<TenantDTO> tenants = FACADE.getAllTenants();
        return Response.ok().entity(GSON.toJson(tenants)).build();
    }
    @GET
    @Path("/{rentalID}")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getTenantsByRentalID(@PathParam("rentalID") int rentalID) {
        List<TenantDTO> tenants = FACADE.getTenantsByRentalID(rentalID);
        return Response.ok().entity(GSON.toJson(tenants)).build();
    }



}
