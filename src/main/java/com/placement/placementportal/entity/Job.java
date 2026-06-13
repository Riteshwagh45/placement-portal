package com.placement.placementportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Double salary;
    private String companyName;
    private String location;

    public Job() {
    }

    public Job(String title, String description, Double salary) {
        this.title = title;
        this.description = description;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getSalary() {
        return salary;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    public String getCompanyName(){
        return companyName;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation(){
        return location;
    }
}