package com.blog.controller;

import com.blog.bo.BlogBO;
import com.blog.model.dto.MainScreenDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Path("/")
public class HomeController {
    @Inject
    BlogBO blogBO;
    private final Template template;

    public HomeController(Template home) {
        this.template = requireNonNull(home, "page is required");
    }

    /**
     * GET / retorna HTML quando Accept: text/html
     * Carrega a página principal
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public TemplateInstance index(){
        return template.instance();
    }

    /**
     * GET /home/posts retorna JSON com a lista de posts
     * Chamado pelo JavaScript para carregar os dados
     */
    @GET
    @Path("/home/posts")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public List<MainScreenDTO> list(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size){
        System.out.println("📌 [HomeController] GET /home/posts chamado com page=" + page + ", size=" + size);
        List<MainScreenDTO> result = blogBO.blogList(page, size);
        System.out.println("📌 [HomeController] Retornando " + result.size() + " posts");
        return result;
    }
}
