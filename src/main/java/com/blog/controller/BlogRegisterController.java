package com.blog.controller;

import com.blog.bo.IBlogBO;
import com.blog.model.dto.BlogRegistrationDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.*;

import static java.util.Objects.requireNonNull;

@Path("/register/blog")
public class BlogRegisterController {
    @Inject
    IBlogBO blogBO;

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
    public Response register(BlogRegistrationDTO dto){
        return blogBO.createBlog(dto);
    }


}
