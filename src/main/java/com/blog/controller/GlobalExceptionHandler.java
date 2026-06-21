package com.blog.controller;

import com.blog.exception.BusinessRuleException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.Map;

public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public RestResponse<Map<String, String>> mapRestResponse(BusinessRuleException exception){
        Map<String, String> jsonResponse = Map.of("error", exception.getMessage());

        return RestResponse.status(Response.Status.BAD_REQUEST, jsonResponse);
    }

    @ServerExceptionMapper
    public RestResponse<Map<String, String>> mapWebApplicationException(WebApplicationException exception){
        String message = exception.getMessage();

        if (message == null || message.isBlank()) {
            message = "Não foi possível completar a ação.";
        }

        Map<String, String> jsonResponse = Map.of("error", message);

        Response.Status status = Response.Status.fromStatusCode(exception.getResponse().getStatus());

        if (status == null) {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return RestResponse.status(status, jsonResponse);
    }
}
