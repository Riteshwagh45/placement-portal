package com.placement.placementportal.repository;

import com.placement.placementportal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository
        extends JpaRepository<Student, Long> {

    Student findByEmail(String email);
}