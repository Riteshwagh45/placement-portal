package com.placement.placementportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String skills;
    private Double cgpa;
    private String resumeUrl;
    private String phone;
    private String address;

    // Default Constructor
    public Student() {
    }

    // Parameterized Constructor
    public Student(String name, String email,
                   String skills, Double cgpa,
                   String resumeUrl,String phone , String address) {

        this.name = name;
        this.email = email;
        this.skills = skills;
        this.cgpa = cgpa;
        this.resumeUrl = resumeUrl;
        this.phone = phone;
        this.address = address;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSkills() {
        return skills;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }
    public String getPhone(){
        return phone;
    }

    public String getAddress() {
        return address;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setAddress(String address){
        this.address = address;
    }
}