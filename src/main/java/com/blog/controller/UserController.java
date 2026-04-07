package com.blog.controller;

import com.blog.model.User;
import io.quarkus.qute.Template;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static java.util.Objects.requireNonNull;

@Path("/cadastrar")
public class UserController {

    private final Template page;

    public UserController(Template page) {this.page = requireNonNull(page, "page is required");}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cadastrar(User user){

        return Response.status(201).entity(user).build();
    }
}
