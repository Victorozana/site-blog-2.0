package com.blog.controller;

import com.blog.bo.LikeBO;
import com.blog.model.dto.LikeResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/blogs")
public class LikeController {
    @Inject
    LikeBO likeBO;
    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/{blogId}/like")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public Response register(@PathParam("blogId") Long blogId){
        Long userId = currentUserId();

        likeBO.create(userId, blogId);

        LikeResponseDTO response = likeBO.list(blogId, userId);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{blogId}/likes")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public Response listSummary(@PathParam("blogId") Long blogId) {

        Long userId = null;

        // Tenta extrair o ID do usuário (Lida com o cenário onde um visitante não logado está lendo o blog)
        try {
            Object claim = jwt.getClaim("idUser");
            if (claim != null) {
                userId = Long.valueOf(claim.toString());
            }
        } catch (Exception e) {
            // Ignora: O usuário não está autenticado, então userId continua null
        }

        // Chama a regra de negócio
        LikeResponseDTO response = likeBO.list(blogId, userId);

        // Retorna o Status 200 OK com o JSON na resposta
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{blogId}/like")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public Response remove(@PathParam("blogId") Long blogId){
        Long userId = currentUserId();

        // 2. Chama a regra de negócio para deletar
        likeBO.remove(userId, blogId);

        LikeResponseDTO response = likeBO.list(blogId, userId);

        return Response.ok(response).build();
    }

    private Long currentUserId() {
        try {
            Object claim = jwt.getClaim("idUser");
            if (claim != null) {
                return Long.valueOf(claim.toString());
            }
        } catch (Exception e) {
            throw new WebApplicationException("Faça login novamente para curtir posts.", Response.Status.UNAUTHORIZED);
        }

        throw new WebApplicationException("Faça login novamente para curtir posts.", Response.Status.UNAUTHORIZED);
    }
}