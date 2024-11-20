package com.risknarrative.spring.exercise.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.risknarrative.spring.exercise.model.SearchRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Collections;

public class TestUtil {

    public static final String APPLICATION_JSON = "application/json";
    public static final String COMPANY_NUMBER = "06500244";
    public static final String COMPANY_NAME = "BBC";
    public static final String COMPANY_RESULTS = readJsonFile("data/company-results.json");;
    public static final String OFFICER_RESULTS = readJsonFile("data/officer-results.json");
    public static ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

    public static JsonNode readFile(final String templatePath) {
        try {
            final Resource resource = new ClassPathResource(templatePath);
            return objectMapper.readTree(resource.getContentAsByteArray());
        } catch (IOException ex) {
            return null;
        }
    }

    public static String readJsonFile(final String templatePath) {
        try {
            final Resource resource = new ClassPathResource(templatePath);
            return IOUtils.toString(resource.getContentAsByteArray());
        } catch (IOException ex) {
            return null;
        }
    }

    public static HttpEntity<SearchRequest> getHttpEntity(String companyName, String companyNumber) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("x-api-key", "apikey");
        return new HttpEntity<>(new SearchRequest(companyName,companyNumber), headers);
    }
}
