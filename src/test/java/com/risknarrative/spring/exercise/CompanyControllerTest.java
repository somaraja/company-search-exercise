package com.risknarrative.spring.exercise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import com.risknarrative.spring.exercise.entity.CompanyBean;
import com.risknarrative.spring.exercise.model.SearchRequest;
import com.risknarrative.spring.exercise.model.SearchResponse;
import com.risknarrative.spring.exercise.repository.CompanyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.risknarrative.spring.exercise.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock({
        @ConfigureWireMock(name = "companyservice", properties = "base.url")
})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CompanyControllerTest {

    @InjectWireMock("companyservice")
    private WireMockServer wiremock;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        wiremock.stubFor(get("/Search?Query=BBC").willReturn(aResponse()
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody(COMPANY_RESULTS)));
        wiremock.stubFor(get("/Search?Query=06500244").willReturn(aResponse()
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody(COMPANY_RESULTS)));
        wiremock.stubFor(get("/Officers?CompanyNumber=06500244").willReturn(aResponse()
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody(OFFICER_RESULTS)));
        wiremock.stubFor(get("/Search?Query=.*").willReturn(aResponse()
                .withHeader("Content-Type", APPLICATION_JSON)
                .withStatus(404)));
        wiremock.stubFor(get("/Officers?CompanyNumber=.*").willReturn(aResponse()
                .withHeader("Content-Type", APPLICATION_JSON)
                .withStatus(404)));
    }

    @AfterEach
    void cleanUp() {
        companyRepository.deleteById(COMPANY_NUMBER);
        wiremock.resetRequests();
    }

    @Test
    void testActiveOfficers() throws JsonProcessingException {
        final HttpEntity<SearchRequest> entity = getHttpEntity(COMPANY_NAME, COMPANY_NUMBER);
        ResponseEntity<SearchResponse> responseEntity = restTemplate.exchange("/search?active=true", HttpMethod.POST, entity, SearchResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final SearchResponse response = responseEntity.getBody();

        JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(response));
        JsonNode expected = readFile("data/active-officers-success.json");
        assertEquals(expected, actual);

        // If the endpoint is called with companyNumber data being saved in the database for the later use
        Optional<CompanyBean> companyBean = companyRepository.findById(COMPANY_NUMBER);
        assertTrue(companyBean.isPresent());

        CompanyBean companyPersistedBean = companyBean.get();
        assertEquals(COMPANY_NUMBER, companyPersistedBean.getCompanyNumber());

        wiremock.resetRequests();

        // calling the same endpoint again to make sure data retrieved from database as there is a record now and not from endpoint
        restTemplate.exchange("/search?active=true", HttpMethod.POST, entity, SearchResponse.class);
        wiremock.verify(exactly(0), getRequestedFor(urlEqualTo("/Search?Query=06500244")));
    }

    @Test
    void testOfficersReadFromDB() throws JsonProcessingException {
        final HttpEntity<SearchRequest> entity = getHttpEntity(COMPANY_NAME, COMPANY_NUMBER);
        String companyBean = readJsonFile("data/company-bean.json");
        CompanyBean bean = objectMapper.readValue(companyBean, CompanyBean.class);
        companyRepository.save(bean);

        // calling the same endpoint again to make sure data retrieved from database as there is a record now and not from endpoint
        ResponseEntity<SearchResponse> responseEntity = restTemplate.exchange("/search?active=true", HttpMethod.POST, entity, SearchResponse.class);

        wiremock.verify(exactly(0), getRequestedFor(urlEqualTo("/Search?Query=06500244")));
        JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(responseEntity.getBody()));
        JsonNode expected = readFile("data/active-officers-success.json");
        assertEquals(expected, actual);
    }


    @Test
    void testAllOfficers() throws JsonProcessingException {
        final HttpEntity<SearchRequest> entity = getHttpEntity("BBC", "06500244");
        final ResponseEntity<SearchResponse> responseEntity = restTemplate.exchange("/search?active=false", HttpMethod.POST, entity, SearchResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final SearchResponse response = responseEntity.getBody();
        JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(response));
        JsonNode expected = readFile("data/search-response-success.json");
        assertEquals(expected, actual);
    }

    @Test
    void testWhenNoResponseFound() {
        final HttpEntity<SearchRequest> entity = getHttpEntity("ITV", "9999999");
        final ResponseEntity<SearchResponse> responseEntity = restTemplate.exchange("/api?active=false", HttpMethod.POST, entity, SearchResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}