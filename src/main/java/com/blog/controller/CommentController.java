package com.blog.controller;

import com.blog.bo.ICommentBO;
import com.blog.model.dto.CommentResponseDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/blogs")
public class CommentController {
    @Inject
    ICommentBO commentBO;

    @POST
    @Path("/{blogId}/comment")
    public Response register(@PathParam("blogId") Long blogId, @Context SecurityContext securityContext){
        String user = securityContext.getUserPrincipal().getName();
        Long userId = Long.valueOf(user);

        commentBO.create(userId, blogId);

        return Response.ok(Response.Status.CREATED).build();
    }
}
