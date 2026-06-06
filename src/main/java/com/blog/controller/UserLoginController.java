package com.blog.controller;

import com.blog.bo.UserBO;
import com.blog.model.dto.LoginRequestDTO;
import com.blog.model.dto.LoginResponseDTO;
import com.blog.model.entity.User;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Path("/login")
public class    UserLoginController {
    @Inject
    UserBO userBO;
    private final Template page;

    public UserLoginController(Template userLogin) {
        this.page = requireNonNull(userLogin, "page is required");
    }

    @Path("/auth")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO credentials){
        LoginResponseDTO response = userBO.login(credentials);

        NewCookie jwtCookie = new NewCookie.Builder("token")
                .value(response.getToken())
                .path("/")
                .maxAge(15 * 24 * 60 * 60)
                .httpOnly(true)
                // .secure(true) descomentar quando for para produção
                .build();

        NewCookie userCookie = new NewCookie.Builder("userName")
                .value(response.getName())
                .path("/")
                .maxAge(15 * 24 * 60 * 60)
                .httpOnly(false)
                .build();

        return Response.ok(response)
                .cookie(jwtCookie)
                .cookie(userCookie)
                .build();
    }

//
//    @GET
//    @Path("/users")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<User> findAll(){
//        return userBO.listAll();
//    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance page_load(){
        return page.instance();
    }
}