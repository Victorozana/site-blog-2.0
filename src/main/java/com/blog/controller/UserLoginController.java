package com.blog.controller;

import com.blog.bo.UserBO;
import com.blog.bo.AuditLogBO;
import com.blog.model.dto.LoginRequestDTO;
import com.blog.model.dto.LoginResponseDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;

import static java.util.Objects.requireNonNull;

@Path("/login")
public class    UserLoginController {
    @Inject
    UserBO userBO;
    @Inject
    AuditLogBO auditLogBO;
    @Inject
    JsonWebToken jwt;
    private final Template page;

    public UserLoginController(Template userLogin) {
        this.page = requireNonNull(userLogin, "page is required");
    }

    @Path("/auth")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO credentials){
        LoginResponseDTO response = userBO.login(credentials);

        // 1. O Cookie de Segurança (Cego para o JS)
        NewCookie jwtCookie = new NewCookie.Builder("meu_token_jwt")
                .value(response.getToken())
                .path("/").maxAge(15 * 24 * 60 * 60).httpOnly(true).build();

        // 2. Os Cookies de Interface (Para o JS ler)
        NewCookie nameCookie = new NewCookie.Builder("userName")
                .value(response.getName())
                .path("/").maxAge(15 * 24 * 60 * 60).httpOnly(false).build();

        NewCookie roleCookie = new NewCookie.Builder("userType")
                .value(response.getUserType()) // Certifique-se de retornar o tipo de usuário no DTO
                .path("/").maxAge(15 * 24 * 60 * 60).httpOnly(false).build();

        NewCookie userIdCookie = new NewCookie.Builder("userId")
                .value(response.getIdUser().toString())
                .path("/").maxAge(15 * 24 * 60 * 60).httpOnly(false).build();

        return Response.ok(response)
                .cookie(jwtCookie).cookie(nameCookie).cookie(roleCookie).cookie(userIdCookie)
                .build();
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance page_load(){
        return page.instance();
    }

    @POST
    @Path("/logout")
    public Response logout() {
        try {
            Object claimId = jwt.getClaim("idUser");
            Object claimName = jwt.getClaim("name");

            if (claimId != null) {
                auditLogBO.log(
                        "LOGOUT",
                        Long.valueOf(claimId.toString()),
                        claimName != null ? claimName.toString() : null,
                        null,
                        "Usuário saiu do sistema."
                );
            }
        } catch (Exception e) {
            // Se o token já estiver inválido, apenas limpa os cookies.
        }

        NewCookie jwtCookie = new NewCookie.Builder("meu_token_jwt")
                .value("")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();

        NewCookie nameCookie = new NewCookie.Builder("userName")
                .value("")
                .path("/")
                .maxAge(0)
                .httpOnly(false)
                .build();

        NewCookie roleCookie = new NewCookie.Builder("userType")
                .value("")
                .path("/")
                .maxAge(0)
                .httpOnly(false)
                .build();

        NewCookie userIdCookie = new NewCookie.Builder("userId")
                .value("")
                .path("/")
                .maxAge(0)
                .httpOnly(false)
                .build();

        return Response.noContent()
                .cookie(jwtCookie)
                .cookie(nameCookie)
                .cookie(roleCookie)
                .cookie(userIdCookie)
                .build();
    }
}
