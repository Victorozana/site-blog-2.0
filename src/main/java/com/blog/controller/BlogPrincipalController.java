package com.blog.controller;

import com.blog.bo.IBlogBO;
import com.blog.model.dto.BlogPrincipalDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/blogs")
public class BlogPrincipalController {
    @Inject
    IBlogBO blogBO;

    @GET
    @Path("/blog/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String blog(@PathParam("id") Long id){
        BlogPrincipalDTO dto = blogBO.findBlogById(id);
        return null; //TODO replace this stub to something useful
    }
}
