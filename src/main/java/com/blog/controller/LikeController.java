package com.blog.controller;

import com.blog.bo.ILikeBO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/blogs")
public class LikeController {
    @Inject
    ILikeBO likeBO;
    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/{blogId}/likes")
    public Response register(@PathParam("blogId") Long blogId){
        Object claim = jwt.getClaim("idUser");
        Long userId = Long.valueOf(claim.toString());

        likeBO.create(userId, blogId);

        return Response.ok(Response.Status.CREATED).build();
    }
}
