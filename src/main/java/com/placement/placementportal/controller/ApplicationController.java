package com.placement.placementportal.controller;

import com.placement.placementportal.entity.Application;
import com.placement.placementportal.repository.ApplicationRepository;
import com.placement.placementportal.repository.StudentRepository;
import com.placement.placementportal.repository.JobRepository;
import com.placement.placementportal.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.placement.placementportal.entity.Student;
import com.placement.placementportal.entity.Job;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApplicationRepository applicationRepository;

    // Show All Applications
    @GetMapping
    public String viewApplications(Model model) {
        model.addAttribute("applications", applicationRepository.findAll());
        return "applications";
    }

    // Open Add Application Form
    @GetMapping("/add")
    public String showAddApplicationForm(Model model) {
        model.addAttribute("application", new Application());
        return "add-application";
    }

    // Save Application
    @PostMapping("/save")
    public String saveApplication(@ModelAttribute Application application) {
        applicationRepository.save(application);
        return "redirect:/applications";
    }

    // Open Edit Form
    @GetMapping("/edit/{id}")
    public String editApplication(@PathVariable Long id, Model model) {
        Application application = applicationRepository.findById(id).orElse(null);
        if (application == null) {
            return "redirect:/applications";
        }
        model.addAttribute("application", application);
        return "edit-application";
    }

    // Update Application
    @PostMapping("/update")
    public String updateApplication(@ModelAttribute Application application) {
        applicationRepository.save(application);
        return "redirect:/applications";
    }

    // Delete Application
    @GetMapping("/delete/{id}")
    public String deleteApplication(@PathVariable Long id) {
        applicationRepository.deleteById(id);
        return "redirect:/applications";
    }

    @PostMapping("/update-status")
    public String updateStatus(
            @RequestParam Long id,
            @RequestParam String status) {

        System.out.println("ID = " + id);
        System.out.println("STATUS = "+ status);

        Application application =
                applicationRepository.findById(id)
                        .orElse(null);

        if (application != null) {

            application.setStatus(status);

            applicationRepository.save(application);

            Student student =
                    studentRepository.findById(
                                    Long.valueOf(application.getStudentId()))
                            .orElse(null);

            Job job =
                    jobRepository.findById(
                                    Long.valueOf(application.getJobId()))
                            .orElse(null);

            if (student != null && job != null) {

                String subject =
                        "Placement Application Status Update";

                String message = "";

                if ("SELECTED".equals(status)) {

                    message =
                            "Congratulations " +
                                    student.getName() + "!\n\n" +

                                    "You have been SELECTED.\n\n" +

                                    "Company : " + job.getCompanyName() + "\n" +
                                    "Role : " + job.getTitle() + "\n" +
                                    "Package : ₹" + job.getSalary() + "\n" +
                                    "Location : " + job.getLocation() + "\n\n" +

                                    "Best Wishes,\nPlacement Portal Team";
                }

                else if ("SHORTLISTED".equals(status)) {

                    message =
                            "Congratulations " +
                                    student.getName() + "!\n\n" +

                                    "You have been SHORTLISTED.\n\n" +

                                    "Company : " + job.getCompanyName() + "\n" +
                                    "Role : " + job.getTitle() + "\n" +
                                    "Location : " + job.getLocation() + "\n\n" +

                                    "Best Wishes,\nPlacement Portal Team";
                }

                else if ("REJECTED".equals(status)) {

                    message =
                            "Dear " +
                                    student.getName() + ",\n\n" +

                                    "We regret to inform you that your application was not selected.\n\n" +

                                    "Company : " + job.getCompanyName() + "\n" +
                                    "Role : " + job.getTitle() + "\n\n" +

                                    "Keep improving and apply again.\n\n" +

                                    "Placement Portal Team";
                }

                if (!message.isEmpty()) {

                    try {
                        emailService.sendEmail(
                                student.getEmail(),
                                subject,
                                message);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        return "redirect:/applications";
    }

    // View Resume PDF
    @GetMapping("/resume/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> viewResume(@PathVariable String fileName) throws Exception {
        Path path = Paths.get("uploads").resolve(fileName);
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}