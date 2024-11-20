package com.risknarrative.spring.exercise.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.risknarrative.spring.exercise.entity.AddressBean;
import com.risknarrative.spring.exercise.entity.CompanyBean;
import com.risknarrative.spring.exercise.entity.OfficerBean;
import com.risknarrative.spring.exercise.model.Address;
import com.risknarrative.spring.exercise.model.SearchOfficer;
import com.risknarrative.spring.exercise.model.SearchRequest;
import com.risknarrative.spring.exercise.model.SearchResponseItem;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class TestDataUtils {

    public static final String APPLICATION_JSON = "application/json";

    public static final String COMPANY_NUMBER = "06500244";

    public static final String COMPANY_NAME = "BBC";

    public static final String TITLE =  "BBC LIMITED";

    public static final String COMPANY_STATUS = "active";

    public static final String COMPANY_TYPE = "ltd";

    public static final String LOCALITY = "Retford";

    public static final String POSTAL_CODE = "DN22 0AD";

    public static final String PREMISES = "Boswell Cottage Main Street";

    public static final String ADDRESS_LINE1 = "North Leverton";

    public static final String COUNTRY = "England";

    public static final String  OFFICER_NAME = "ANTLES, Kerri";

    public static final String OFFICER_ROLE = "director";

    public static final LocalDate APPOINTED_ON = LocalDate.of(2017, 4, 1);

    public static final LocalDate DATE_OF_CREATION  = LocalDate.of(2008, 2, 11);

    public static final String COMPANY_RESULTS = readJsonFile("data/company-results.json");

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
            return IOUtils.toString(resource.getContentAsByteArray(), StandardCharsets.UTF_8.name());
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

    public static SearchResponseItem defaultSearchResponseItem() {
        return SearchResponseItem.builder()
                .address(getAddress())
                .companyNumber(COMPANY_NUMBER)
                .companyStatus(COMPANY_STATUS)
                .companyType(COMPANY_TYPE)
                .dateOfCreation(DATE_OF_CREATION)
                .title(TITLE)
                .officers(List.of(getSearchOfficer()))
                .build();
    }

    public static Address getAddress() {
        return Address.builder()
                .locality(LOCALITY)
                .postalCode(POSTAL_CODE)
                .premises(PREMISES)
                .addressLine1(ADDRESS_LINE1)
                .country(COUNTRY)
                .build();
    }

    public static SearchOfficer getSearchOfficer() {
        return SearchOfficer.builder()
                .name(OFFICER_NAME)
                .officerRole(OFFICER_ROLE)
                .appointedOn(APPOINTED_ON)
                .address(getAddress())
                .build();
    }

    public static AddressBean getAddressBean() {
        AddressBean addressBean = new AddressBean();

        addressBean.setLocality(LOCALITY);
        addressBean.setPostalCode(POSTAL_CODE);
        addressBean.setPremises(PREMISES);
        addressBean.setAddressLine1(ADDRESS_LINE1);
        addressBean.setCountry(COUNTRY);

        return addressBean;
    }

    public static OfficerBean getOfficerBean() {
        OfficerBean officerBean = new OfficerBean();

        officerBean.setName(OFFICER_NAME);
        officerBean.setOfficerRole(OFFICER_ROLE);
        officerBean.setAppointedOn(APPOINTED_ON);
        officerBean.setAddress(getAddressBean());

        return officerBean;
    }

    public static CompanyBean getDefaultCompanyBean() {
        CompanyBean companyBean = new CompanyBean();

        companyBean.setCompanyNumber(COMPANY_NUMBER);
        companyBean.setCompanyType(COMPANY_TYPE);
        companyBean.setTitle(TITLE);
        companyBean.setCompanyStatus(COMPANY_STATUS);
        companyBean.setDateOfCreation(DATE_OF_CREATION);
        companyBean.setAddress(getAddressBean());
        companyBean.setOfficers(List.of(getOfficerBean()));

        return companyBean;
    }
}
