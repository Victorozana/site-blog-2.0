package com.blog.security;

import io.smallrye.jwt.build.Jwt;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.RsaKeyUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class JwtValidationTest {

    private JwtConsumer jwtConsumer;
    private String token;

    @BeforeEach
    void setup() throws Exception {
        // Carregar a chave pública
        String publicKeyPem = loadPublicKey();
        PublicKey publicKey = new RsaKeyUtil().fromPemEncoded(publicKeyPem);
        
        // Configurar o consumidor JWT
        this.jwtConsumer = new JwtConsumerBuilder()
                .setVerificationKey(publicKey)
                .setExpectedIssuer("http://localhost:8080")
                .setSkipDefaultAudienceValidation()
                .build();

        // Gerar um token de teste
        this.token = Jwt.issuer("http://localhost:8080")
                .upn("test@example.com")
                .groups(new HashSet<>(Arrays.asList("USER", "ADMIN")))
                .claim("idUser", 1L)
                .claim("name", "Test User")
                .claim("userType", "ADMIN")
                .expiresIn(Duration.ofDays(15))
                .sign();

        System.out.println("✓ Token gerado: " + token);
    }

    @Test
    void testTokenValidation() throws Exception {
        assertNotNull(token, "Token não deve ser nulo");
        assertFalse(token.isEmpty(), "Token não deve estar vazio");
        
        // Validar o token
        JwtClaims claims = jwtConsumer.processToClaims(token);
        
        assertNotNull(claims, "Claims não devem ser nulas");
        assertEquals("test@example.com", claims.getStringClaimValue("upn"));
        assertEquals(1L, claims.getClaimValue("idUser"));
        assertEquals("Test User", claims.getStringClaimValue("name"));
        
        System.out.println("✓ Token validado com sucesso!");
        System.out.println("  - UPN: " + claims.getStringClaimValue("upn"));
        System.out.println("  - User ID: " + claims.getClaimValue("idUser"));
        System.out.println("  - Name: " + claims.getStringClaimValue("name"));
    }

    private String loadPublicKey() throws Exception {
        InputStream resourceStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("publicKey.pem");
        if (resourceStream == null) {
            throw new RuntimeException("Arquivo publicKey.pem não encontrado");
        }
        return new String(resourceStream.readAllBytes());
    }
}
