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

    @PATCH // PATCH é o verbo REST correto quando atualizamos apenas um campo de um recurso
    @Path("/profile-picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER","READER","ADMIN"})
    public UserProfileDTO uploadProfilePicture(@RestForm("file") FileUpload file) {
        String savedImageUrl = imageStorageBO.save(file);

        return userBO.updateProfilePicture(currentUserId(), savedImageUrl);
    }

    @GET
    @Path("/uploads/images/{fileName}")
    public Response getImage(@PathParam("fileName") String fileName) {

        // Vai até a pasta onde o LocalStorageService salvou as fotos
        File file = new File("uploads/images/" + fileName);

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
            return Response.serverError().build();
        }
    }

    private Long currentUserId() {
        Object claim = jwt.getClaim("idUser");

        if (claim == null) {
            throw new WebApplicationException("Sessão inválida. Faça login novamente.", Response.Status.UNAUTHORIZED);
        }

        return Long.valueOf(claim.toString());
    }
}
