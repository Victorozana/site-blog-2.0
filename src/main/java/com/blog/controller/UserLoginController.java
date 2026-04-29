package com.blog.controller;

import com.blog.bo.UserBO;
import com.blog.model.dto.UserLoginDTO;
import com.blog.model.entity.User;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Path("/login")
public class    UserLoginController {
    @Inject
    UserBO userBO;
    private final Template page;

    public UserLoginController(Template loginUsuario) {
        this.page = requireNonNull(loginUsuario, "page is required");
    }

    @Path("/auth")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserLoginDTO user){
        return userBO.login(user);
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> findAll(){
        return userBO.listAll();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance page_load(){
        return page.instance();
    }
}