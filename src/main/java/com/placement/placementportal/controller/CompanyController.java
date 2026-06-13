package com.placement.placementportal.controller;

import com.placement.placementportal.entity.Company;
import com.placement.placementportal.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/add")
    public String showAddCompanyForm(Model model) {

        model.addAttribute("company", new Company());

        return "add-company";
    }

    @PostMapping("/save")
    public String saveCompany(@ModelAttribute Company company) {

        companyRepository.save(company);

        return "redirect:/companies";
    }

    @GetMapping("/edit/{id}")
    public String editCompany(@PathVariable Long id,
                              Model model) {

        Company company =
                companyRepository.findById(id).orElse(null);

        model.addAttribute("company", company);

        return "edit-company";
    }

    @PostMapping("/update")
    public String updateCompany(
            @ModelAttribute Company company) {

        companyRepository.save(company);

        return "redirect:/companies";
    }

    @GetMapping("/delete/{id}")
    public String deleteCompany(
            @PathVariable Long id) {

        companyRepository.deleteById(id);

        return "redirect:/companies";
    }
}