package com.risknarrative.spring.exercise.api;

import com.risknarrative.spring.exercise.model.SearchRequest;
import com.risknarrative.spring.exercise.model.SearchResponse;
import com.risknarrative.spring.exercise.service.SearchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/search")
@Slf4j
public class CompanyController {
    private final SearchService searchService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchResponse> searchCompanyOfficers(@Valid @RequestBody SearchRequest searchRequest,
                                                                @NotBlank @RequestHeader("x-api-key") final String apiKey,
                                                                @RequestParam(value = "active", required = false) boolean isActive) {
        if(!searchRequest.isValid())
            return ResponseEntity.badRequest().build();

        RequestContextHolder.getRequestAttributes().setAttribute("x-api-key", apiKey, WebRequest.SCOPE_REQUEST);
        return ResponseEntity.ok(searchService.searchCompanyOfficers(searchRequest, isActive));
    }

}
