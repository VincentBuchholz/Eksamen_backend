package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.TenantDTO;
import facades.RentalFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final UserFacade FACADE =  UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response createUser(String content) {
        TenantDTO tenantDTO = GSON.fromJson(content, TenantDTO.class);
        System.out.println(tenantDTO);
        TenantDTO newTenantDTO = FACADE.createUser(tenantDTO);
        return Response.ok().entity(GSON.toJson(newTenantDTO)).build();
    }
}
