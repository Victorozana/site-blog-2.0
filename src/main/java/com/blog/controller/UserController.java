package com.blog.controller;

import com.blog.bo.UserBO;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Path("/cadastro")
public class UserController {
    @Inject
    UserBO userBO;
    private final Template page;

    public UserController(Template cadastroUsuario) {
        this.page = requireNonNull(cadastroUsuario, "page is required");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrar(UserRegistrationDTO user){
        userBO.saveUser(user);
        return Response.status(201).entity(user).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance page_load(){
        return page.instance();
    }
}
