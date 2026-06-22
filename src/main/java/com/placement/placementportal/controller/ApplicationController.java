package com.placement.placementportal.controller;

import com.placement.placementportal.entity.Application;
import com.placement.placementportal.entity.User;
import com.placement.placementportal.entity.Company;
import com.placement.placementportal.repository.ApplicationRepository;
import com.placement.placementportal.repository.StudentRepository;
import com.placement.placementportal.repository.JobRepository;
import com.placement.placementportal.repository.CompanyRepository;
import com.placement.placementportal.service.EmailService;

import jakarta.servlet.http.HttpSession;
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
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private CompanyRepository companyRepository;

    // Show All Applications (Filtered Contextually)
    @GetMapping
    public String viewApplications(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        List<Application> applications;

        if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
            Company company = companyRepository.findByEmail(loggedUser.getEmail());
            if (company == null) {
                return "redirect:/login";
            }

            String compName = company.getCompanyName();
            List<Long> companyJobIds = jobRepository.findAll().stream()
                    .filter(j -> j.getCompanyName() != null && j.getCompanyName().equalsIgnoreCase(compName))
                    .map(Job::getId)
                    .collect(Collectors.toList());

            applications = applicationRepository.findAll().stream()
                    .filter(app -> app.getJobId() != null && companyJobIds.contains(app.getJobId()))
                    .collect(Collectors.toList());
        } else {
            applications = applicationRepository.findAll();
        }

        model.addAttribute("applications", applications);
        return "applications";
    }

    // New Endpoint: Added handling for specific job mappings requested by dashboard UI template tracker
    @GetMapping("/job/{jobId}")
    public String viewApplicationsByJob(@PathVariable Long jobId, Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        Job job = jobRepository.findById(jobId).orElse(null);

        if (job == null) {
            return "redirect:/applications";
        }

        // Secure protection layer check
        if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
            Company company = companyRepository.findByEmail(loggedUser.getEmail());
            if (company == null || job.getCompanyName() == null || !job.getCompanyName().equalsIgnoreCase(company.getCompanyName())) {
                return "redirect:/company-dashboard";
            }
        }

        List<Application> applications = applicationRepository.findByJobId(jobId);
        model.addAttribute("applications", applications);
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
    public String editApplication(@PathVariable Long id, Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        Application application = applicationRepository.findById(id).orElse(null);
        if (application == null) {
            return "redirect:/applications";
        }

        if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
            Company company = companyRepository.findByEmail(loggedUser.getEmail());
            Job job = jobRepository.findById(application.getJobId()).orElse(null);
            if (company == null || job == null || job.getCompanyName() == null || !job.getCompanyName().equalsIgnoreCase(company.getCompanyName())) {
                return "redirect:/applications";
            }
        }

        model.addAttribute("application", application);
        return "edit-application";
    }

    // Update Application
    @PostMapping("/update")
    public String updateApplication(@ModelAttribute Application application, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
            Application existingApp = applicationRepository.findById(application.getId()).orElse(null);
            if (existingApp == null) return "redirect:/applications";

            Job job = jobRepository.findById(existingApp.getJobId()).orElse(null);
            Company company = companyRepository.findByEmail(loggedUser.getEmail());
            if (company == null || job == null || job.getCompanyName() == null || !job.getCompanyName().equalsIgnoreCase(company.getCompanyName())) {
                return "redirect:/applications";
            }
        }

        applicationRepository.save(application);
        return "redirect:/applications";
    }

    // Delete Application
    @GetMapping("/delete/{id}")
    public String deleteApplication(@PathVariable Long id, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        Application application = applicationRepository.findById(id).orElse(null);

        if (application != null) {
            if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
                Company company = companyRepository.findByEmail(loggedUser.getEmail());
                Job job = jobRepository.findById(application.getJobId()).orElse(null);
                if (company == null || job == null || job.getCompanyName() == null || !job.getCompanyName().equalsIgnoreCase(company.getCompanyName())) {
                    return "redirect:/applications";
                }
            }
            applicationRepository.deleteById(id);
        }

        return "redirect:/applications";
    }

    @PostMapping("/update-status")
    public String updateStatus(
            @RequestParam Long id,
            @RequestParam String status,
            HttpSession session) {

        User loggedUser = (User) session.getAttribute("loggedUser");
        Application application = applicationRepository.findById(id).orElse(null);

        if (application != null) {
            // Verify structural context boundaries before status mutation handling
            if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
                Company company = companyRepository.findByEmail(loggedUser.getEmail());
                Job currentJob = jobRepository.findById(application.getJobId()).orElse(null);
                if (company == null || currentJob == null || currentJob.getCompanyName() == null ||
                        !currentJob.getCompanyName().equalsIgnoreCase(company.getCompanyName())) {
                    return "redirect:/applications";
                }
            }

            application.setStatus(status);
            applicationRepository.save(application);

            Student student = studentRepository.findById(Long.valueOf(application.getStudentId())).orElse(null);
            Job job = jobRepository.findById(Long.valueOf(application.getJobId())).orElse(null);

            if (student != null && job != null) {
                String subject = "Placement Application Status Update";
                String message = "";

                if ("SELECTED".equals(status)) {
                    message = "Congratulations " + student.getName() + "!\n\n" +
                            "You have been SELECTED.\n\n" +
                            "Company : " + job.getCompanyName() + "\n" +
                            "Role : " + job.getTitle() + "\n" +
                            "Package : ₹" + job.getSalary() + "\n" +
                            "Location : " + job.getLocation() + "\n\n" +
                            "Best Wishes,\nPlacement Portal Team";
                } else if ("SHORTLISTED".equals(status)) {
                    message = "Congratulations " + student.getName() + "!\n\n" +
                            "You have been SHORTLISTED.\n\n" +
                            "Company : " + job.getCompanyName() + "\n" +
                            "Role : " + job.getTitle() + "\n" +
                            "Location : " + job.getLocation() + "\n\n" +
                            "Best Wishes,\nPlacement Portal Team";
                } else if ("REJECTED".equals(status)) {
                    message = "Dear " + student.getName() + ",\n\n" +
                            "We regret to inform you that your application was not selected.\n\n" +
                            "Company : " + job.getCompanyName() + "\n" +
                            "Role : " + job.getTitle() + "\n\n" +
                            "Keep improving and apply again.\n\n" +
                            "Placement Portal Team";
                }

                if (!message.isEmpty()) {
                    try {
                        emailService.sendEmail(student.getEmail(), subject, message);
                    } catch(Exception e){
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
    public ResponseEntity<Resource> viewResume(@PathVariable String fileName, HttpSession session) throws Exception {
        User loggedUser = (User) session.getAttribute("loggedUser");

        // Security checks: ensure the requested document belongs to this recruiter's applicant tracking scope
        if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
            Company company = companyRepository.findByEmail(loggedUser.getEmail());
            if (company != null) {
                String compName = company.getCompanyName();
                boolean matchesRecruiter = applicationRepository.findAll().stream()
                        .filter(app -> app.getResumeUrl() != null && app.getResumeUrl().contains(fileName))
                        .map(app -> jobRepository.findById(app.getJobId()).orElse(null))
                        .anyMatch(job -> job != null && job.getCompanyName() != null && job.getCompanyName().equalsIgnoreCase(compName));

                if (!matchesRecruiter) {
                    return ResponseEntity.status(403).build(); // Block unauthorized cross-access
                }
            }
        }

        Path path = Paths.get("uploads").resolve(fileName);
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}