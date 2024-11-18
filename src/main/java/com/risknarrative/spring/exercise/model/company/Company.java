package com.risknarrative.spring.exercise.model.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {

    @JsonProperty("page_number")
    public Integer pageNumber;
    public String kind;
    @JsonProperty("total_results")
    public Integer totalResults;
    public List<CompanyItem> items;

}
