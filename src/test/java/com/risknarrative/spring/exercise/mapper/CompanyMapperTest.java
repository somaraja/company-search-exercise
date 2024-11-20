package com.risknarrative.spring.exercise.mapper;

import com.risknarrative.spring.exercise.entity.AddressBean;
import com.risknarrative.spring.exercise.entity.CompanyBean;
import com.risknarrative.spring.exercise.model.SearchResponseItem;
import com.risknarrative.spring.exercise.util.TestDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.risknarrative.spring.exercise.util.TestDataUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CompanyMapperTest {

    private static final CompanyBean COMPANY_BEAN = TestDataUtils.getDefaultCompanyBean();

    private static final AddressBean ADDRESS_BEAN = TestDataUtils.getAddressBean();

    private static final SearchResponseItem SEARCH_RESPONSE_ITEM = TestDataUtils.defaultSearchResponseItem();

    private CompanyMapper mapper;

    @BeforeEach
    void init() {
        mapper = new CompanyMapperImpl();
    }

    @Test
    void shouldReturnNullFromBean() {
        assertNull(mapper.toCompanyBean(null));
    }

    @Test
    void shouldReturnNullFromPojo() {
        assertNull(mapper.toSearchResponseItem(null));
    }

    @Test
    void shouldMapToCompanyBean() {
        CompanyBean companyBean = mapper.toCompanyBean(SEARCH_RESPONSE_ITEM);
        assertEquals(COMPANY_NUMBER, companyBean.getCompanyNumber());
        assertEquals(COMPANY_TYPE, companyBean.getCompanyType());
        assertEquals(TITLE, companyBean.getTitle());
        assertEquals(COMPANY_STATUS, companyBean.getCompanyStatus());
        assertEquals(DATE_OF_CREATION, companyBean.getDateOfCreation());
        assertEquals(ADDRESS_BEAN, companyBean.getAddress());
        assertEquals(Arrays.asList(getOfficerBean()), companyBean.getOfficers());
    }

    @Test
    void shouldMapToSearchResponseItemPojo() {
        SearchResponseItem searchResponseItem = mapper.toSearchResponseItem(COMPANY_BEAN);
        assertEquals(COMPANY_NUMBER, searchResponseItem.companyNumber);
        assertEquals(COMPANY_TYPE, searchResponseItem.companyType);
        assertEquals(TITLE, searchResponseItem.title);
        assertEquals(COMPANY_STATUS, searchResponseItem.companyStatus);
        assertEquals(DATE_OF_CREATION, searchResponseItem.dateOfCreation);
        assertEquals(getAddress().toString(), searchResponseItem.address.toString());
        assertEquals(Arrays.asList(getSearchOfficer()).stream().findFirst().toString(),
                searchResponseItem.officers.stream().findFirst().toString());
    }

    @Test
    void testEnrichCompanyBean() {
        CompanyBean companyBean = mapper.enrichCompanyBean(COMPANY_BEAN);
        assertEquals(COMPANY_NUMBER, companyBean.getCompanyNumber());
        assertEquals(COMPANY_TYPE, companyBean.getCompanyType());
        assertEquals(TITLE, companyBean.getTitle());
        assertEquals(COMPANY_STATUS, companyBean.getCompanyStatus());
        assertEquals(DATE_OF_CREATION, companyBean.getDateOfCreation());
        assertEquals(ADDRESS_BEAN, companyBean.getAddress());
    }
}
