package com.risknarrative.spring.exercise.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Addresses")
public class AddressBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String locality;
    private String postalCode;
    private String premises;
    private String addressLine1;
    private String country;
}

