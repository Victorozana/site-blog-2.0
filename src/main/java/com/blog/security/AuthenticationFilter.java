package com.blog.security;

import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.RsaKeyUtil;

import java.io.InputStream;
import java.security.PublicKey;
import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String PUBLIC_KEY_PATH = "publicKey.pem";
    private static final String EXPECTED_ISSUER = "http://localhost:8080";
    private JwtConsumer jwtConsumer;

    public AuthenticationFilter() {
        try {
            String publicKeyPem = loadPublicKey();
            PublicKey publicKey = new RsaKeyUtil().fromPemEncoded(publicKeyPem);
            
            this.jwtConsumer = new JwtConsumerBuilder()
                   .setVerificationKey(publicKey)
                   .setExpectedIssuer(EXPECTED_ISSUER)
                   .setSkipDefaultAudienceValidation()
                   .build();
        } catch (Exception e) {
            System.err.println("ERRO ao carregar chave pública JWT: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        if (isPublicRoute(path)) {
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring("Bearer ".length());
        }

        if (token == null || token.isEmpty()) {
            rejectRequest(requestContext, "Token não fornecido");
            return;
        }

        try {
            JwtClaims claims = jwtConsumer.processToClaims(token);
            requestContext.setProperty("jwtClaims", claims);
            System.out.println("✓ Token validado com sucesso para: " + claims.getStringClaimValue("upn"));
        } catch (Exception e) {
            System.err.println("✗ Erro ao validar JWT: " + e.getMessage());
            rejectRequest(requestContext, "Token inválido ou expirado");
        }
    }

    private void rejectRequest(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(
               Response.status(Response.Status.UNAUTHORIZED)
                       .entity("{\"error\": \"" + message + "\"}")
                       .build()
        );
    }

    private String loadPublicKey() throws Exception {
        InputStream resourceStream = Thread.currentThread().getContextClassLoader()
               .getResourceAsStream(PUBLIC_KEY_PATH);
        if (resourceStream == null) {
            throw new RuntimeException("Arquivo publicKey.pem não encontrado no classpath");
        }
        return new String(resourceStream.readAllBytes());
    }

    private boolean isPublicRoute(String path) {
        return path.startsWith("/login") ||
               path.startsWith("/register/user") ||
               path.equals("/") ||
               path.startsWith("/api/posts");
    }
}
