package com.placement.placementportal.repository;

import com.placement.placementportal.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    Application findByStudentIdAndJobId(
            Long studentId,
            Long jobId);

    List<Application> findByStudentId(
            Long studentId);

    List<Application> findByJobId(
            Long jobId);
}