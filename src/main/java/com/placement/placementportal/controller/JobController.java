package com.placement.placementportal.controller;

import com.placement.placementportal.entity.Job;
import com.placement.placementportal.entity.User;
import com.placement.placementportal.entity.Company;
import com.placement.placementportal.repository.JobRepository;
import com.placement.placementportal.repository.CompanyRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.placement.placementportal.repository.ApplicationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public String listJobs(
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpSession session,
            Model model) {

        User loggedUser = (User) session.getAttribute("loggedUser");
        List<Job> jobs;

        // Check if the logged-in user is a Company
        if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
            Company company = companyRepository.findByEmail(loggedUser.getEmail());
            if (company == null) {
                return "redirect:/login";
            }

            String compName = company.getCompanyName();

            if (keyword != null && !keyword.trim().isEmpty()) {
                // Fetch globally filtered matches first
                List<Job> globalMatches = jobRepository
                        .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrLocationContainingIgnoreCase(
                                keyword, keyword, keyword, keyword
                        );
                // Filter down explicitly to this company's listings only
                jobs = globalMatches.stream()
                        .filter(j -> j.getCompanyName() != null && j.getCompanyName().equalsIgnoreCase(compName))
                        .collect(Collectors.toList());
                model.addAttribute("keyword", keyword);
            } else {
                // Return only this company's jobs
                jobs = jobRepository.findAll().stream()
                        .filter(j -> j.getCompanyName() != null && j.getCompanyName().equalsIgnoreCase(compName))
                        .collect(Collectors.toList());
            }
        } else {
            // Admin or general fall-through logic untouched
            if (keyword != null && !keyword.trim().isEmpty()) {
                jobs = jobRepository
                        .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrLocationContainingIgnoreCase(
                                keyword, keyword, keyword, keyword
                        );
                model.addAttribute("keyword", keyword);
            } else {
                jobs = jobRepository.findAll();
            }
        }

        model.addAttribute("jobs", jobs);
        return "jobs";
    }

    @GetMapping("/add")
    public String showAddJobForm(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        Job job = new Job();

        // Auto-fill company name if recruiter is adding a job
        if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
            Company company = companyRepository.findByEmail(loggedUser.getEmail());
            if (company != null) {
                job.setCompanyName(company.getCompanyName());
            }
        }

        model.addAttribute("job", job);
        return "add-job";
    }

    @PostMapping("/save")
    public String saveJob(@ModelAttribute Job job, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        // Security constraint forcing correct ownership identification
        if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
            Company company = companyRepository.findByEmail(loggedUser.getEmail());
            if (company != null) {
                job.setCompanyName(company.getCompanyName());
            }
        }

        jobRepository.save(job);
        return "redirect:/jobs";
    }

    @GetMapping("/edit/{id}")
    public String editJob(@PathVariable Long id, Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        Job job = jobRepository.findById(id).orElse(null);

        if (job == null) {
            return "redirect:/jobs";
        }

        // Security Validation
        if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
            Company company = companyRepository.findByEmail(loggedUser.getEmail());
            if (company == null || job.getCompanyName() == null || !job.getCompanyName().equalsIgnoreCase(company.getCompanyName())) {
                return "redirect:/jobs"; // Prevent editing other company's profiles
            }
        }

        model.addAttribute("job", job);
        return "edit-job";
    }

    @PostMapping("/update")
    public String updateJob(@ModelAttribute Job job, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
            Company company = companyRepository.findByEmail(loggedUser.getEmail());
            Job existingJob = jobRepository.findById(job.getId()).orElse(null);

            if (company == null || existingJob == null || existingJob.getCompanyName() == null ||
                    !existingJob.getCompanyName().equalsIgnoreCase(company.getCompanyName())) {
                return "redirect:/jobs";
            }
            // Enforce parity constraints on modification
            job.setCompanyName(company.getCompanyName());
        }

        jobRepository.save(job);
        return "redirect:/jobs";
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        Job job = jobRepository.findById(id).orElse(null);

        if (job != null) {
            if (loggedUser != null && "COMPANY".equalsIgnoreCase(loggedUser.getRole())) {
                Company company = companyRepository.findByEmail(loggedUser.getEmail());
                if (company == null || job.getCompanyName() == null || !job.getCompanyName().equalsIgnoreCase(company.getCompanyName())) {
                    return "redirect:/jobs";
                }
            }
            jobRepository.deleteById(id);
        }

        return "redirect:/jobs";
    }
}