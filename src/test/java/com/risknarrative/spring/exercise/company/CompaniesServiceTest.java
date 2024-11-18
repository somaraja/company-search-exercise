package com.risknarrative.spring.exercise.company;

import com.risknarrative.spring.exercise.service.CompanyService;
import com.risknarrative.spring.exercise.model.company.Company;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompaniesServiceTest {

    @Mock
    private RestTemplate mockRestTemplate;

    @Test
    void searchCompany() {
        final CompanyService companiesService = new CompanyService(mockRestTemplate);
        final Company expectedResponse = new Company();
        expectedResponse.kind = "SomeKind";
        final ResponseEntity<Company> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Company.class)))
                .thenReturn(responseEntity);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        RequestContextHolder.getRequestAttributes().setAttribute("x-api-key", "api-key", WebRequest.SCOPE_REQUEST);
       final Company response = companiesService.findCompanyDetails("TEST1");

       assertNotNull(response);
       assertEquals(expectedResponse, response);
       assertEquals("SomeKind", response.kind);
    }
}