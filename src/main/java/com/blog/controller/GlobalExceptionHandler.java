package com.blog.controller;

import com.blog.exception.BusinessRuleException;
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
}