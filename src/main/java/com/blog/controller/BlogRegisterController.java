package com.blog.controller;

import com.blog.bo.IBlogBO;
import com.blog.model.dto.BlogRegistrationDTO;
import com.blog.model.dto.BlogResponseDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import static java.util.Objects.requireNonNull;

@Path("/register/blog")
public class BlogRegisterController {
    @Inject
    IBlogBO blogBO;
    @Inject
    JsonWebToken jwt;

    private final Template page;

    public BlogRegisterController(Template blogRegister) {
        this.page = requireNonNull(blogRegister, "page is required");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance page_load(){
        return page.instance();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("WRITER")
    public Response register(BlogRegistrationDTO dto){
        // 1. Pegamos o objeto genérico do JSON, seja ele qual for (Parsson, Jackson, etc)
        Object claimIdUser = jwt.getClaim("idUser");

        // 2. Convertemos para texto e parseamos nativamente para Long (A Blindagem)
        Long idAuthor = Long.parseLong(claimIdUser.toString());

        BlogResponseDTO responseDTO = blogBO.createBlog(dto, idAuthor);

        return Response.ok(Response.Status.CREATED).entity(responseDTO).build();
    }
}
