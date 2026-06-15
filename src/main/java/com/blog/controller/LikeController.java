package com.blog.controller;

import com.blog.bo.ILikeBO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/blogs")
public class LikeController {
    @Inject
    ILikeBO likeBO;

    @POST
    @Path("/{blogId}/likes")
    public Response register(@PathParam("blogId") Long blogId, @Context SecurityContext securityContext){
        String user = securityContext.getUserPrincipal().getName();
        Long userId = Long.valueOf(user);

        likeBO.create(userId, blogId);

        return Response.ok(Response.Status.CREATED).build();
    }
}
