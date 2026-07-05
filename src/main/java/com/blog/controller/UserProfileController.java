package com.blog.controller;

import java.io.File;
import java.nio.file.Files;
import com.blog.bo.ImageStorageBO;
import com.blog.bo.UserBO;
import com.blog.model.dto.UserProfileDTO;
import com.blog.model.dto.UserProfileUpdateDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
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
    @RolesAllowed({"WRITER","READER","ADMIN"})
    public TemplateInstance page(){
        return template.instance();
    }

    // retorna o perfil público do usuário passando o ID
    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER","READER","ADMIN"})
    public UserProfileDTO me() {
        return userBO.getPublicProfile(currentUserId());
    }

    @PATCH
    @Path("/profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER","READER","ADMIN"})
    public UserProfileDTO updateProfile(UserProfileUpdateDTO dto) {
        return userBO.updateProfile(currentUserId(), dto);
    }

    // atualiza a imagem do usuário
    @PATCH
    @Path("/profile-picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER","READER","ADMIN"})
    public UserProfileDTO uploadProfilePicture(@RestForm("file") FileUpload file) {
        String savedImageUrl = imageStorageBO.save(file);

        return userBO.updateProfilePicture(currentUserId(), savedImageUrl);
    }

    // deleta a conta do usuário
    @DELETE
    @Path("/me")
    @RolesAllowed({"WRITER","READER","ADMIN"})
    public Response deleteAccount() {
        userBO.deleteUser(currentUserId());

        return Response.noContent()
                .cookie(expiredCookie("meu_token_jwt", true))
                .cookie(expiredCookie("userName", false))
                .cookie(expiredCookie("userType", false))
                .cookie(expiredCookie("userId", false))
                .build();
    }

    // pega a imagem atual do usuário
    @GET
    @Path("/uploads/images/{fileName}")
    public Response getImage(@PathParam("fileName") String fileName) {

        // Vai até a pasta onde o LocalStorageService salvou as fotos
        File file = new File("uploads/images/" + fileName);

        // se a imagem não existe, retorna resposta 404
        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            String contentType = Files.probeContentType(file.toPath());

            if (contentType == null || !contentType.startsWith("image/")) {
                contentType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return Response.ok(file, contentType).build();
        } catch (Exception e) {
            // caso cair em alguma exceção dispara error, resposta 500
            return Response.serverError().build();
        }
    }

    // retorna o ID do usuário atual
    private Long currentUserId() {
        Object claim = jwt.getClaim("idUser");

        if (claim == null) {
            throw new WebApplicationException("Sessão inválida. Faça login novamente.", Response.Status.UNAUTHORIZED);
        }

        return Long.valueOf(claim.toString());
    }

    // expira o cookie para realizar o logout
    private NewCookie expiredCookie(String name, boolean httpOnly) {
        return new NewCookie.Builder(name)
                .value("")
                .path("/")
                .maxAge(0)
                .httpOnly(httpOnly)
                .build();
    }
}
