package com.placement.placementportal.controller;

import com.placement.placementportal.entity.Company;
import com.placement.placementportal.entity.Job;
import com.placement.placementportal.entity.User;
import com.placement.placementportal.entity.Application;
import com.placement.placementportal.repository.ApplicationRepository;
import com.placement.placementportal.repository.CompanyRepository;
import com.placement.placementportal.repository.JobRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CompanyDashboardController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @GetMapping("/company-dashboard")
    public String companyDashboard(HttpSession session, Model model) {

        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/login";
        }

        Company company = companyRepository.findByEmail(loggedUser.getEmail());

        if (company == null) {
            return "redirect:/login";
        }

        List<Job> companyJobs = new ArrayList<>();

        for (Job job : jobRepository.findAll()) {
            if (job.getCompanyName() != null &&
                    job.getCompanyName().equalsIgnoreCase(company.getCompanyName())) {
                companyJobs.add(job);
            }
        }

        long totalJobs = companyJobs.size();

        // Fetching exact context applications matching company's jobs
        List<Application> companyApplications = applicationRepository.findAll()
                .stream()
                .filter(app -> companyJobs.stream()
                        .anyMatch(job -> job.getId() != null && job.getId().equals(app.getJobId())))
                .collect(Collectors.toList());

        long totalApplicants = companyApplications.size();

        // Dynamically compute exact selection index metrics
        long selectedStudents = companyApplications.stream()
                .filter(app -> "SELECTED".equalsIgnoreCase(app.getStatus()))
                .count();

        model.addAttribute("company", company);

        model.addAttribute("companyName", company.getCompanyName());
        model.addAttribute("companyEmail", company.getEmail());
        model.addAttribute("companyLocation", company.getLocation());
        model.addAttribute("companyDescription", company.getDescription());

        model.addAttribute("totalJobs", totalJobs);
        model.addAttribute("activeJobs", totalJobs);
        model.addAttribute("totalApplicants", totalApplicants);
        model.addAttribute("selectedStudents", selectedStudents);

        model.addAttribute("recentJobs", companyJobs);
        model.addAttribute("recentApplicants", companyApplications); // Correctly pipe filtered applicant flows

        return "company-dashboard";
    }
}