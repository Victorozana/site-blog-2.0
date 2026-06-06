package com.blog.controller;

import com.blog.bo.BlogBO;
import com.blog.bo.IBlogBO;
import com.blog.model.dto.BlogScreenDTO;
import com.blog.model.dto.MainScreenDTO;
import com.blog.security.RequireRole;
import com.blog.model.category.UserType;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import static java.util.Objects.requireNonNull;

@Path("/blog")
public class BlogScreenController {
    private final Template template;
    private final IBlogBO blogBO;

    // Injeção unificada por construtor (Padrão recomendado pelo Quarkus)
    public BlogScreenController(Template blogScreen, IBlogBO blogBO){
        this.template = requireNonNull(blogScreen, "page is required");
        this.blogBO = requireNonNull(blogBO, "blogBO is required");
    }

    @GET
    @RequireRole({UserType.writer, UserType.reader})
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance page_load(){
        return template.instance();
    }

    @GET
    @Path("/data")
    @RequireRole({UserType.writer, UserType.reader})
    @Produces(MediaType.APPLICATION_JSON)
    public BlogScreenDTO blog(@QueryParam("id") Long id){
        return blogBO.findBlogById(id);
    }

}
