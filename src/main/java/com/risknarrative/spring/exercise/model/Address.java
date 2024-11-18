package com.risknarrative.spring.exercise.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

    public String premises;
    @JsonProperty("postal_code")
    public String postalCode;
    public String country;
    public String locality;
    @JsonProperty("address_line_1")
    public String addressLine1;

}

