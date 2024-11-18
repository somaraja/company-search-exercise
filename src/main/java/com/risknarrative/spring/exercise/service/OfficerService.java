package com.risknarrative.spring.exercise.service;

import com.risknarrative.spring.exercise.model.officer.Officer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OfficerService implements ApiService {
    @Value("${base.url}")
    private String baseURL;

    final RestTemplate restTemplate;

    /**
     * find the officers for a company from TruProxyAPI endpoint
     * @param companyNumber
     * @return
     */
    public Officer findOfficers(@NonNull final String companyNumber) {
        return restTemplate.exchange(String.join("",baseURL ,"/Officers?CompanyNumber=",companyNumber), HttpMethod.GET, getHttpEntity(), Officer.class).getBody();
    }
}

