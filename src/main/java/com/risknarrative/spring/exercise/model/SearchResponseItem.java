package com.risknarrative.spring.exercise.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class SearchResponseItem {

    @JsonProperty("company_number")
    public String companyNumber;
    @JsonProperty("company_type")
    public String companyType;
    public String title;
    @JsonProperty("company_status")
    public String companyStatus;
    @JsonProperty("date_of_creation")
    public LocalDate dateOfCreation;
    public Address address;
    public List<SearchOfficer> officers;

}

