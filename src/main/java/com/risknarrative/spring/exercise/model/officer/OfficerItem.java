package com.risknarrative.spring.exercise.model.officer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.risknarrative.spring.exercise.model.Address;

import java.time.LocalDate;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficerItem {

    public Address address;
    public String name;
    @JsonProperty("appointed_on")
    public LocalDate appointedOn;
    @JsonProperty("resigned_on")
    public LocalDate resignedOn;
    @JsonProperty("officer_role")
    public String officerRole;
    public OfficerLinks links;
    @JsonProperty("date_of_birth")
    public OfficerDateOfBirth dateOfBirth;
    public String occupation;
    @JsonProperty("country_of_residence")
    public String countryOfResidence;
    public String nationality;

}

