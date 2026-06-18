package com.blog.controller;

import java.io.File;
import com.blog.bo.ImageStorageBO;
import com.blog.bo.UserBO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestForm;

import static java.util.Objects.requireNonNull;

@Path("/user")
public class UserProfileController {
    private final Template template;

    @Inject
    ImageStorageBO imageStorageBO;

    @Inject
    UserBO userBO;

    @Inject
    JsonWebToken jwt;

    public UserProfileController(Template profile){
        this.template = requireNonNull(profile, "page is required");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed({"WRITER","READER"})
    public TemplateInstance page(){
        return template.instance();
    }

    @PATCH // PATCH é o verbo REST correto quando atualizamos apenas um campo de um recurso
    @Path("/profile-picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadProfilePicture(@RestForm("file") FileUpload file) {

        // 1. Descobre quem é o usuário baseado no token da requisição
        Long userId = Long.valueOf(jwt.getClaim("idUser").toString());

        // 2. Salva o arquivo fisicamente na pasta e pega a URL (ex: "/uploads/images/foto.jpg")
        String savedImageUrl = imageStorageBO.save(file);

        // 3. Atualiza o banco de dados
        userBO.updateProfilePicture(userId, savedImageUrl);

        // Retorna sucesso
        return Response.noContent().build();
    }

    @GET
    @Path("/uploads/images/{fileName}")
    @Produces("image/jpeg") // O navegador vai entender que o retorno é uma imagem
    public Response getImage(@PathParam("fileName") String fileName) {

        // Vai até a pasta onde o LocalStorageService salvou as fotos
        File file = new File("uploads/images/" + fileName);

        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // O JAX-RS/Quarkus é inteligente o suficiente para pegar o objeto File e enviar os bytes pela rede automaticamente
        return Response.ok(file).build();
    }
}