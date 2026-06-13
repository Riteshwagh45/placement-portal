package com.placement.placementportal.controller;

import com.placement.placementportal.repository.StudentRepository;
import com.placement.placementportal.repository.CompanyRepository;
import com.placement.placementportal.repository.JobRepository;
import com.placement.placementportal.repository.ApplicationRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @GetMapping("/")
    public String root() {

        return "redirect:/login";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(
            HttpSession session,
            Model model) {

        Object loggedUser =
                session.getAttribute("loggedUser");

        if (loggedUser == null) {

            return "redirect:/login";
        }

        model.addAttribute(
                "studentCount",
                studentRepository.count());

        model.addAttribute(
                "companyCount",
                companyRepository.count());

        model.addAttribute(
                "jobCount",
                jobRepository.count());

        model.addAttribute(
                "applicationCount",
                applicationRepository.count());

        return "index";
    }

    @GetMapping("/students")
    public String students(Model model) {

        model.addAttribute(
                "students",
                studentRepository.findAll());

        return "students";
    }

    @GetMapping("/companies")
    public String companies(Model model) {

        model.addAttribute(
                "companies",
                companyRepository.findAll());

        return "companies";
    }

    @GetMapping("/jobs")
    public String jobs(Model model) {

        model.addAttribute(
                "jobs",
                jobRepository.findAll());

        return "jobs";
    }


}