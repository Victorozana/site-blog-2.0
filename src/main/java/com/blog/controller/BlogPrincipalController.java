package com.blog.controller;

import com.blog.bo.IBlogBO;
import com.blog.model.dto.BlogPrincipalDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/blogs")
public class BlogPrincipalController {
    @Inject
    IBlogBO blogBO;

    @GET
    @Path("/blog/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BlogPrincipalDTO blog(@PathParam("id") Long id){
        return blogBO.findBlogById(id);
    }
}
