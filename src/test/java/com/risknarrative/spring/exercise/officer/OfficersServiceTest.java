package com.risknarrative.spring.exercise.officer;

import com.risknarrative.spring.exercise.service.OfficerService;
import com.risknarrative.spring.exercise.model.officer.Officer;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfficersServiceTest {

    @Mock
    private RestTemplate mockRestTemplate;

    @Test
    void searchOfficers() {
        final OfficerService officersService = new OfficerService(mockRestTemplate);
        final Officer expectedResponse = new Officer();
        expectedResponse.kind = "SomeKind";
        final ResponseEntity<Officer> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Officer.class)))
                .thenReturn(responseEntity);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        RequestContextHolder.getRequestAttributes().setAttribute("x-api-key", "api-key", WebRequest.SCOPE_REQUEST);
        final Officer response = officersService.findOfficers("TEST1");

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        assertEquals("SomeKind", response.kind);
    }
}