package com.blog.security;

import com.blog.model.category.UserType;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Arrays;

@Provider
@RequireRole
@Priority(Priorities.AUTHORIZATION)
public class RoleAuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    Principal principal;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Obtém o método chamado
        Method method = resourceInfo.getResourceMethod();
        if (method == null) return;

        // Obtém a anotação do método
        RequireRole requireRole = method.getAnnotation(RequireRole.class);
        if (requireRole == null || requireRole.value().length == 0) {
            return; // Sem restrição de role
        }

        // Obtém o tipo de usuário do JWT
        if (principal instanceof JWTCallerPrincipal) {
            JWTCallerPrincipal jwt = (JWTCallerPrincipal) principal;
            String userType = (String) jwt.getClaim("userType");

            // Verifica se o userType está autorizado
            boolean authorized = Arrays.stream(requireRole.value())
                    .map(UserType::name)
                    .anyMatch(type -> type.equals(userType));

            if (!authorized) {
                requestContext.abortWith(
                        Response.status(Response.Status.FORBIDDEN)
                                .entity("{\"error\": \"Acesso negado. Permissão insuficiente.\"}")
                                .build()
                );
            }
        }
    }
}
