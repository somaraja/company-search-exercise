package com.risknarrative.spring.exercise.model.officer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Officer {

    public String etag;
    public OfficerLinks links;
    public String kind;
    @JsonProperty("items_per_page")
    public Integer itemsPerPage;
    public List<OfficerItem> items;

}

