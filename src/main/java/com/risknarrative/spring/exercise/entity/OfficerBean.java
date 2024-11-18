package com.risknarrative.spring.exercise.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Officers")
public class OfficerBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long officerId;

    private String name;
    private String officerRole;
    private LocalDate appointedOn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressBean address;

    @ManyToOne
    @JoinColumn(name = "company_number", nullable = false)
    private CompanyBean company;
}
