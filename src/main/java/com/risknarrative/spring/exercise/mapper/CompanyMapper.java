package com.risknarrative.spring.exercise.mapper;


import com.risknarrative.spring.exercise.entity.CompanyBean;
import com.risknarrative.spring.exercise.entity.OfficerBean;
import com.risknarrative.spring.exercise.model.SearchResponseItem;
import org.mapstruct.*;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface CompanyMapper {

    SearchResponseItem toSearchResponseItem(final CompanyBean companyBean);

    CompanyBean toCompanyBean(final SearchResponseItem searchResponseItem);

    @Mapping(target="officers", expression = "java(linkOfficers(companyBean))")
    CompanyBean enrichCompanyBean(final CompanyBean companyBean);

    //To associate OfficerBean to CompanyBean
    @Named("linkOfficers")
    default List<OfficerBean> linkOfficers(final CompanyBean companyBean) {
        List<OfficerBean> officerBeans = new ArrayList<>();
        companyBean.getOfficers().forEach(officer -> officerBeans.add(officer));
        officerBeans.stream().forEach(officer -> officer.setCompany(companyBean));

        return officerBeans;
    }

}