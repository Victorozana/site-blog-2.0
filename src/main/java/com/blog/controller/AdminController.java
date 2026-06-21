package com.blog.controller;

import com.blog.bo.AuditLogBO;
import com.blog.bo.UserBO;
import com.blog.model.dto.AdminUserDTO;
import com.blog.model.dto.AuditLogDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Path("/admin")
@RolesAllowed("ADMIN")
public class AdminController {
    private final Template template;

    @Inject
    UserBO userBO;

    @Inject
    AuditLogBO auditLogBO;

    public AdminController(Template admin) {
        this.template = requireNonNull(admin, "page is required");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance page() {
        return template.instance();
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AdminUserDTO> users() {
        return userBO.listUsersForAdmin();
    }

    @GET
    @Path("/audit")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuditLogDTO> audit(@QueryParam("limit") @DefaultValue("80") int limit) {
        return auditLogBO.listRecent(limit);
    }
}
