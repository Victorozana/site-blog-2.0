package com.blog.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class RequestFilter implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("Filter executado");
        System.out.println("Metodo: "+requestContext.getMethod());
        System.out.println("URI: "+requestContext.getUriInfo().getRequestUri());
        System.out.println("Headers: "+requestContext.getHeaders());
        System.out.println("Cookies: "+requestContext.getCookies());
        System.out.println("Path: "+requestContext.getUriInfo().getPath());
        System.out.println("Content-Type: "+requestContext.getMediaType());
    }
}
