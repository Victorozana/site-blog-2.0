package com.blog.controller;

import com.blog.bo.IBlogBO;
import com.blog.model.dto.BlogPrincipalDTO;
import com.blog.model.entity.Blog;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.print.attribute.standard.Media;
import java.util.List;

@Path("/blogs")
public class BlogPrincipalController {
    @Inject
    IBlogBO blogBO;

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
}
