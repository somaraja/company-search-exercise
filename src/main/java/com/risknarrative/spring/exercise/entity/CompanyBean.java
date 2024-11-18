package com.risknarrative.spring.exercise.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Companies")
public class CompanyBean {

    @Id
    private String companyNumber;

    private String companyType;
    private String title;
    private String companyStatus;
    private LocalDate dateOfCreation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressBean address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OfficerBean> officers;
}
