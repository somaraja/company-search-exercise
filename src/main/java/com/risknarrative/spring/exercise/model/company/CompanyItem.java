package com.risknarrative.spring.exercise.model.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.risknarrative.spring.exercise.model.Address;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyItem {

    @JsonProperty("company_status")
    public String companyStatus;
    @JsonProperty("address_snippet")
    public String addressSnippet;
    @JsonProperty("date_of_creation")
    public LocalDate dateOfCreation;
    public Matches matches;
    public String description;
    public CompanyLinks links;
    @JsonProperty("company_number")
    public String companyNumber;
    public String title;
    @JsonProperty("company_type")
    public String companyType;
    public Address address;
    public String kind;
    @JsonProperty("description_identifier")
    public List<String> descriptionIdentifier;

}


