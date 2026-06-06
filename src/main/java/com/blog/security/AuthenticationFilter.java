package com.blog.security;

import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Rotas públicas que não precisam de autenticação
        String path = requestContext.getUriInfo().getPath();
        if (isPublicRoute(path)) {
            return;
        }

        // Verifica se há token JWT no header Authorization ou no cookie
        String authHeader = requestContext.getHeaderString("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring("Bearer ".length());
        }

        if (token == null) {
            // Se não houver token, rejeita com 401
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{\"error\": \"Token inválido ou expirado\"}")
                            .build()
            );
        }
    }

    private boolean isPublicRoute(String path) {
        // Rotas que não precisam de autenticação
        return path.startsWith("/login") ||
               path.startsWith("/register/user") ||
               path.equals("/") ||
               path.startsWith("/api/posts");
    }
}
