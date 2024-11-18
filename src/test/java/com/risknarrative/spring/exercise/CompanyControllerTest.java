package com.risknarrative.spring.exercise;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import com.risknarrative.spring.exercise.entity.CompanyBean;
import com.risknarrative.spring.exercise.model.SearchResponseItem;
import com.risknarrative.spring.exercise.model.SearchOfficer;
import com.risknarrative.spring.exercise.model.SearchRequest;
import com.risknarrative.spring.exercise.model.SearchResponse;
import com.risknarrative.spring.exercise.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock({
    @ConfigureWireMock(name = "companyservice", properties = "base.url")
})
class CompanyControllerTest {

    @InjectWireMock("companyservice")
    private WireMockServer wiremock;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    final static String COMPANY_RESULTS =
            """
            {
                "page_number": 1,
                "kind": "search#companies",
                "total_results": 20,
                "items": [
                    {
                        "company_status": "active",
                        "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
                        "date_of_creation": "2008-02-11",
                        "matches": {
                            "title": [
                                1,
                                3
                            ]
                        },
                        "description": "06500244 - Incorporated on 11 February 2008",
                        "links": {
                            "self": "/company/06500244"
                        },
                        "company_number": "06500244",
                        "title": "BBC LIMITED",
                        "company_type": "ltd",
                        "address": {
                            "premises": "Boswell Cottage Main Street",
                            "postal_code": "DN22 0AD",
                            "country": "England",
                            "locality": "Retford",
                            "address_line_1": "North Leverton"
                        },
                        "kind": "searchresults#company",
                        "description_identifier": [
                            "incorporated-on"
                        ]
                    }]
            }
            """;

    final static String OFFICER_RESULTS =
            """
              {
                "etag": "6dd2261e61776d79c2c50685145fac364e75e24e",
                "links": {
                    "self": "/company/10241297/officers"
                },
                "kind": "officer-list",
                "items_per_page": 35,
                "items": [
                    {
                        "address": {
                            "premises": "The Leeming Building",
                            "postal_code": "LS2 7JF",
                            "country": "England",
                            "locality": "Leeds",
                            "address_line_1": "Vicar Lane"
                        },
                        "name": "ANTLES, Kerri",
                        "appointed_on": "2017-04-01",
                        "resigned_on": "2018-02-12",
                        "officer_role": "director",
                        "links": {
                            "officer": {
                                "appointments": "/officers/4R8_9bZ44w0_cRlrxoC-wRwaMiE/appointments"
                            }
                        },
                        "date_of_birth": {
                            "month": 6,
                            "year": 1969
                        },
                        "occupation": "Finance And Accounting",
                        "country_of_residence": "United States",
                        "nationality": "American"
                    }]
              }
            """;


    @BeforeEach
    void beforeTest() {
        wiremock.stubFor(get("/Search?Query=BBC").willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(COMPANY_RESULTS)));
        wiremock.stubFor(get("/Search?Query=06500244").willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(COMPANY_RESULTS)));
        wiremock.stubFor(get("/Officers?CompanyNumber=06500244").willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(OFFICER_RESULTS)));
        wiremock.stubFor(get("/Search?Query=.*").willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(404)));
        wiremock.stubFor(get("/Officers?CompanyNumber=.*").willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(404)));
    }

    @Test
    void testActiveOfficers() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("x-api-key", "apikey");
        final HttpEntity<SearchRequest> entity = new HttpEntity<>(new SearchRequest("BBC","06500244"), headers);
        ResponseEntity<SearchResponse> responseEntity = restTemplate.exchange("/search?active=true", HttpMethod.POST, entity, SearchResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final SearchResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(1, response.getTotalResults());
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().size());
        final SearchResponseItem item = response.getItems().get(0);
        assertNotNull(item);
        assertNotNull(item.officers);

        // If the endpoint is called with companyNumber data being saved in the database for the later use
        Optional<CompanyBean> companyBean = companyRepository.findById("06500244");
        assertNotNull(companyBean);

        wiremock.resetRequests();

        // calling the same endpoint again to make sure data retrieved from database as there is a record now and not from endpoint
        restTemplate.exchange("/search?active=true", HttpMethod.POST, entity, SearchResponse.class);
        wiremock.verify(exactly(0), getRequestedFor(urlEqualTo("/Search?Query=06500244")));
    }

    @Test
    void testAllOfficers() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("x-api-key", "apikey");
        final HttpEntity<SearchRequest> entity = new HttpEntity<>(new SearchRequest("BBC","06500244"), headers);
        final ResponseEntity<SearchResponse> responseEntity = restTemplate.exchange("/search?active=false", HttpMethod.POST, entity, SearchResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final SearchResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(1, response.getTotalResults());
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().size());
        final SearchResponseItem item = response.getItems().get(0);
        assertNotNull(item);
        assertEquals(LocalDate.parse("2008-02-11"), item.dateOfCreation);
        assertEquals("06500244", item.companyNumber);
        assertNotNull(item.address);
        assertEquals("North Leverton", item.address.addressLine1);
        assertEquals("England", item.address.country);
        assertEquals("Retford", item.address.locality);
        assertEquals("DN22 0AD", item.address.postalCode);
        assertEquals("Boswell Cottage Main Street", item.address.premises);

        assertEquals("active", item.companyStatus);
        assertEquals("BBC LIMITED", item.title);
        assertEquals("ltd", item.companyType);
        assertNotNull(item.officers);
        assertEquals(1, item.officers.size());
        final SearchOfficer officer = item.officers.get(0);
        assertNotNull(officer);
        assertEquals(LocalDate.parse("2017-04-01"), officer.appointedOn);
        assertEquals("director", officer.officerRole);
        assertEquals("ANTLES, Kerri", officer.name);
        assertNotNull(officer.address);
        assertEquals("Vicar Lane", officer.address.addressLine1);
        assertEquals("England", officer.address.country);
        assertEquals("Leeds", officer.address.locality);
        assertEquals("LS2 7JF", officer.address.postalCode);
        assertEquals("The Leeming Building", officer.address.premises);
    }

    @Test
    void testWhenNoResponseFound() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("x-api-key", "apikey");
        final HttpEntity<SearchRequest> entity = new HttpEntity<>(new SearchRequest("ITV","9999999"), headers);
        final ResponseEntity<SearchResponse> responseEntity = restTemplate.exchange("/api?active=false", HttpMethod.POST, entity, SearchResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}