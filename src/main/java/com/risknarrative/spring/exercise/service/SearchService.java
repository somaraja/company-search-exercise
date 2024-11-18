package com.risknarrative.spring.exercise.service;

import com.risknarrative.spring.exercise.entity.CompanyBean;
import com.risknarrative.spring.exercise.exception.CompanyNotFoundException;
import com.risknarrative.spring.exercise.mapper.CompanyMapper;
import com.risknarrative.spring.exercise.model.SearchOfficer;
import com.risknarrative.spring.exercise.model.SearchRequest;
import com.risknarrative.spring.exercise.model.SearchResponse;
import com.risknarrative.spring.exercise.model.company.Company;
import com.risknarrative.spring.exercise.model.SearchResponseItem;
import com.risknarrative.spring.exercise.model.company.CompanyItem;
import com.risknarrative.spring.exercise.model.officer.Officer;
import com.risknarrative.spring.exercise.model.officer.OfficerItem;
import com.risknarrative.spring.exercise.repository.CompanyRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final CompanyRepository companyRepository;

    private final CompanyService companiesService;
    private final OfficerService officersService;

    private final CompanyMapper companyMapper;

    /**
     * searchCompanyOfficers will process the company and officer details from TruProxyAPI endpoint
     *   and map to the response
     * @param searchRequest
     * @param isActive
     * @return
     */
    public SearchResponse searchCompanyOfficers(SearchRequest searchRequest, boolean isActive) {
        if (StringUtils.hasText(searchRequest.getCompanyNumber())) {
            //If request contains company number, as per bonus task first searching in the database,
            // if record exists then will be fetched from there otherwise the endpoint will be invoked
            Optional<CompanyBean> companyBean = companyRepository.findById(searchRequest.getCompanyNumber());
            if(companyBean.isPresent()) {
                final SearchResponse searchResponse = new SearchResponse();
                final SearchResponseItem searchResponseItem =  companyMapper.toSearchResponseItem(companyBean.get());
                searchResponse.items = List.of(searchResponseItem);
                searchResponse.totalResults = searchResponse.items.size();

                return searchResponse;
            }
        }

        return buildSearchResponse(searchRequest, isActive);
    }

    /**
     * mapping resultant company and officer details to SearchResponse
     * @param searchRequest
     * @param isActive
     * @return
     */
    private SearchResponse buildSearchResponse(final SearchRequest searchRequest, boolean isActive) {
        boolean isCompanyNumber = StringUtils.hasText(searchRequest.getCompanyNumber());
        String companyDetail = isCompanyNumber ? searchRequest.getCompanyNumber() : searchRequest.getCompanyName();
        Company companyResponse = companiesService.findCompanyDetails(companyDetail);

        final SearchResponse searchResponse = new SearchResponse();
        Optional.ofNullable(companyResponse).filter(a-> Objects.requireNonNull(a.totalResults) > 0).orElseThrow(() -> new CompanyNotFoundException(companyDetail));

        searchResponse.items = companyResponse.items.stream().map(item -> findOfficers(item, isActive)).toList();

        //If there is no record in the database, fetched details from endpoint will be saved in the database for later use
        if(isCompanyNumber) {
            searchResponse.items.forEach(item -> {
                CompanyBean company = companyMapper.toCompanyBean(item);
                companyRepository.save(companyMapper.enrichCompanyBean(company));
            });
        }

        searchResponse.totalResults = searchResponse.items.size();

        return searchResponse;
    }

    /**
     * To find officers and map to SearchResponseItem element
     * @param item
     * @param isActive
     * @return
     */
    private SearchResponseItem findOfficers(@NonNull final CompanyItem item, boolean isActive) {
        Officer officer = officersService.findOfficers(item.companyNumber);

        return buildSearchResponseItem(item, officer, isActive);
    }

    /**
     * To map CompanyItem to SearchResponseItem
     * @param companyItem
     * @param officer
     * @param isActive
     * @return
     */
    private SearchResponseItem buildSearchResponseItem(@NonNull final CompanyItem companyItem, @NonNull Officer officer, boolean isActive) {
        return SearchResponseItem.builder()
            .address(companyItem.address)
                .companyNumber(companyItem.companyNumber)
                .companyStatus(companyItem.companyStatus)
                .companyType(companyItem.companyType)
                .dateOfCreation(companyItem.dateOfCreation)
                .title(companyItem.title)
                .officers(officer.items != null ? officer.items.stream()
                        .filter(o -> !isActive || o.resignedOn == null)
                        .map(SearchService::buildOfficerResponse).toList() : Collections.emptyList())
                .build();
    }

    /**
     * To map OfficerItem to SearchOfficer
     * @param officerItem
     * @return
     */
    private static SearchOfficer buildOfficerResponse(OfficerItem officerItem) {
        return SearchOfficer.builder()
        .address(officerItem.address)
        .appointedOn(officerItem.appointedOn)
        .name(officerItem.name)
        .officerRole(officerItem.officerRole)
                .build();
    }

}
