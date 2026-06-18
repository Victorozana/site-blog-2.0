package com.blog.controller;

import com.blog.bo.LikeBO;
import com.blog.model.dto.LikeResponseDTO;
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
    public Response register(@PathParam("blogId") Long blogId){
        Object claim = jwt.getClaim("idUser");
        Long userId = Long.valueOf(claim.toString());

        likeBO.create(userId, blogId);

        return Response.ok(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{blogId}/likes")
    @Produces(MediaType.APPLICATION_JSON)
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
    public Response remove(@PathParam("blogId") Long blogId){
        // 1. Extrai o ID do usuário do Token (garantindo que ele está logado)
        Long userId = null;
        try {
            Object claim = jwt.getClaim("idUser");
            if (claim != null) {
                userId = Long.valueOf(claim.toString());
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // 2. Chama a regra de negócio para deletar
        likeBO.remove(userId, blogId);

        // 3. Retorna 204 No Content (Padrão HTTP para uma exclusão bem-sucedida que não devolve corpo)
        return Response.noContent().build();
    }
}
