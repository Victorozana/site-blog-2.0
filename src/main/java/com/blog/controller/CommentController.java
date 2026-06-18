package com.blog.controller;

import com.blog.bo.ICommentBO;
import com.blog.model.dto.CommentRequestDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/blogs")
public class CommentController {
    @Inject
    ICommentBO commentBO;
    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/{blogId}/comments") // Dica: No padrão REST, usamos o plural (comments)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@PathParam("blogId") Long blogId, CommentRequestDTO request) {
        Object claim = jwt.getClaim("idUser");
        Long userId = Long.valueOf(claim.toString());

        // Passa os IDs e o texto extraído do DTO para o Business Object
        commentBO.create(userId, blogId, request);

        // Forma correta de retornar um Status 201 (Created) no Quarkus
        return Response.status(Response.Status.CREATED).build();
    }
}
