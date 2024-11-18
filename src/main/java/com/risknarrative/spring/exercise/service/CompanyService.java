package com.risknarrative.spring.exercise.service;

import com.risknarrative.spring.exercise.model.company.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CompanyService implements ApiService {

    @Value("${base.url}")
    private String baseURL;

    final private RestTemplate restTemplate;

    /**
     * find company details from TruProxyAPI endpoint
     * @param criteria
     * @return
     */
    public Company findCompanyDetails(final String criteria) {
        return restTemplate.exchange(String.join("",baseURL ,"/Search?Query=", criteria), HttpMethod.GET, getHttpEntity(), Company.class).getBody();
    }
}

