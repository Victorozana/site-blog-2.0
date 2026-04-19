package com.blog.controller;

import com.blog.bo.IBlogBO;
import com.blog.model.dto.BlogRegistrationDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.*;

@Path("/register/blog")
public class BlogRegisterController {
    @Inject
    IBlogBO blogBO;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(BlogRegistrationDTO dto){
        return blogBO.createBlog(dto);
    }
}
