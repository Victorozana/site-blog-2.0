package com.blog.controller;

import com.blog.bo.CommentBO;
import com.blog.model.dto.CommentRequestDTO;
import com.blog.model.dto.CommentResponseDTO;
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
    public Response register(@PathParam("blogId") Long blogId, CommentRequestDTO request) {
        Object claim = jwt.getClaim("idUser");
        Long userId = Long.valueOf(claim.toString());

        // Passa os IDs e o texto extraído do DTO para o Business Object
        commentBO.create(userId, blogId, request);

        // Forma correta de retornar um Status 201 (Created) no Quarkus
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{blogId}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CommentResponseDTO> list(@PathParam("blogId") Long blogId){
        return commentBO.list(blogId);
    }
}
