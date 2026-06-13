package com.placement.placementportal.repository;

import com.placement.placementportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository
        extends JpaRepository<Job, Long> {

}