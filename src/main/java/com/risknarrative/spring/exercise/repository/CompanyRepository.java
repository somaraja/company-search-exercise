package com.risknarrative.spring.exercise.repository;

import com.risknarrative.spring.exercise.entity.CompanyBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyBean, String> {
}

