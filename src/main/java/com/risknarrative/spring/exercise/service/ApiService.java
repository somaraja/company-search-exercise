package com.risknarrative.spring.exercise.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.Objects;


public interface ApiService {
    default HttpEntity getHttpEntity() {

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("x-api-key", (String) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).getAttribute("x-api-key", WebRequest.SCOPE_REQUEST));
        return new HttpEntity<>("parameters", httpHeaders);
    }
}
