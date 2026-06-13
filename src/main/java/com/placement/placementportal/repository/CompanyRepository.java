package com.placement.placementportal.repository;

import com.placement.placementportal.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}