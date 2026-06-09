package com.blog.controller;

import com.blog.bo.IUserBO;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.dto.UserResponseDTO;
import com.blog.model.entity.User;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Path("/register/user")
public class UserRegisterController {
    @Inject
    IUserBO userBO;
    private final Template page;

    public UserRegisterController(Template userRegister) {
        this.page = requireNonNull(userRegister, "page is required");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUser(UserRegistrationDTO user){
        UserResponseDTO dto = userBO.saveUser(user);
        return Response.ok(dto).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance page_load(){
        return page.instance();
    }
}