package com.blog.controller;

import com.blog.bo.CommentBO;
import com.blog.model.dto.CommentRequestDTO;
import com.blog.model.dto.CommentResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/blogs")
public class CommentController {
    @Inject
    CommentBO commentBO;
    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/{blogId}/comment") // Dica: No padrão REST, usamos o plural (comments)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public Response register(@PathParam("blogId") Long blogId, CommentRequestDTO request) {
        Object claim = jwt.getClaim("idUser");

        if (claim == null) {
            throw new WebApplicationException("Usuário não autenticado", Response.Status.UNAUTHORIZED);
        }

        Long userId = Long.valueOf(claim.toString());

        commentBO.create(userId, blogId, request);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{blogId}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public List<CommentResponseDTO> list(@PathParam("blogId") Long blogId){
        return commentBO.list(blogId);
    }
}
