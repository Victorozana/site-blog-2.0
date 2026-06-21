package com.blog.controller;

import com.blog.bo.UserBO;
import com.blog.model.dto.UserProfileDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import static java.util.Objects.requireNonNull;

@Path("/profile")
public class PublicProfileController {
    private final Template template;

    @Inject
    UserBO userBO;

    public PublicProfileController(Template publicProfile) {
        this.template = requireNonNull(publicProfile, "page is required");
    }

    @GET
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public TemplateInstance page() {
        return template.instance();
    }

    @GET
    @Path("/data")
    @jakarta.ws.rs.Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"WRITER", "READER", "ADMIN"})
    public UserProfileDTO publicProfile(@QueryParam("id") Long id) {
        return userBO.getPublicProfile(id);
    }
}
