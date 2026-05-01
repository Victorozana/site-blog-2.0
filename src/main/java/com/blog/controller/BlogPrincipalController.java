package com.blog.controller;

import com.blog.bo.IBlogBO;
import com.blog.model.dto.BlogPrincipalDTO;
import com.blog.model.entity.Blog;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import javax.xml.transform.Templates;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Path("/blogs")
public class BlogPrincipalController {
    @Inject
    IBlogBO blogBO;

    private final Template page;

    public BlogPrincipalController(Template mainScreen) {
        this.page = requireNonNull(mainScreen, "page is required");
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Blog> list(){
        return blogBO.blogList();
    }

    @GET
    @Path("/blog/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BlogPrincipalDTO blog(@PathParam("id") Long id){
        return blogBO.findBlogById(id);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance page_load(){return page.instance();}
}
