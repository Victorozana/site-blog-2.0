package com.blog.controller;

import com.blog.bo.UserBO;
import com.blog.model.dto.UserRegistrationDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static java.util.Objects.requireNonNull;

@Path("/login")
public class UserLoginController {
    @Inject
    UserBO userBO;
    private final Template page;

    public UserLoginController(Template loginUsuario) {
        this.page = requireNonNull(loginUsuario, "page is required");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserRegistrationDTO user){
        userBO.saveUser(user);
        return Response.status(200).entity(user).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance page_load(){
        return page.instance();
    }
}