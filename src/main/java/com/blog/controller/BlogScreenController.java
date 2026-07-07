package com.blog.controller;

import com.blog.bo.BlogBO;
import com.blog.model.dto.BlogRegistrationDTO;
import com.blog.model.dto.BlogResponseDTO;
import com.blog.model.dto.BlogScreenDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Path("/blog")
public class BlogScreenController {
    private final Template template;
    @Inject
    Template managePosts;
    @Inject
    BlogBO blogBO;
    @Inject
    JsonWebToken jwt;

    public BlogScreenController(Template blogScreen){
        this.template = requireNonNull(blogScreen, "page is required");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public TemplateInstance page_load(){
        return template.instance();
    }

    // retorna o post passando ID
    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public BlogScreenDTO blog(@QueryParam("id") Long id){
        return blogBO.findBlogById(id);
    }

    // tela de edição do post
    @GET
    @Path("/manage")
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed({"WRITER", "ADMIN"})
    public TemplateInstance managePage() {
        return managePosts.instance();
    }

    // retorna lista de posts gerenciaveis
    @GET
    @Path("/manage/data")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "ADMIN"})
    public List<BlogResponseDTO> manageablePosts() {
        return blogBO.manageableBlogList(currentUserId(), isAdmin());
    }

    // atualiza post
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "ADMIN"})
    public BlogResponseDTO update(@PathParam("id") Long id, BlogRegistrationDTO dto) {
        return blogBO.updateBlog(id, currentUserId(), isAdmin(), dto);
    }

    // exclui o post
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "ADMIN"})
    public Response delete(@PathParam("id") Long id) {
        blogBO.deleteBlog(id, currentUserId(), isAdmin());
        return Response.noContent().build();
    }

    // pega o usuário atual usando o claim do token
    private Long currentUserId() {
        Object claim = jwt.getClaim("idUser");

        if (claim == null) {
            throw new WebApplicationException("Sessão inválida. Faça login novamente.", Response.Status.UNAUTHORIZED);
        }

        return Long.valueOf(claim.toString());
    }

    // verifica se é admin, true or false, pegando do JWT
    private boolean isAdmin() {
        return jwt.getGroups() != null && jwt.getGroups().contains("ADMIN");
    }

}
