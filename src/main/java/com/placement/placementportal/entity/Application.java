package com.placement.placementportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "job_id")
    private Long jobId;

    private String status;

    private String applicationType;

    @Column(length = 2000)
    private String coverLetter;

    private String resumeUrl;

    public Application() {
    }

    public Application(Long studentId,
                       Long jobId,
                       String status) {
        this.studentId = studentId;
        this.jobId = jobId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getStatus() {
        return status;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }
}