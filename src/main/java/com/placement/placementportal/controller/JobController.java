package com.placement.placementportal.controller;

import com.placement.placementportal.entity.Job;
import com.placement.placementportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.placement.placementportal.entity.Application;
import com.placement.placementportal.repository.ApplicationRepository;

import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    @GetMapping("/add")
    public String showAddJobForm(Model model) {

        model.addAttribute("job", new Job());

        return "add-job";
    }

    @PostMapping("/save")
    public String saveJob(@ModelAttribute Job job) {

        jobRepository.save(job);

        return "redirect:/jobs";
    }

    @GetMapping("/edit/{id}")
    public String editJob(@PathVariable Long id,
                          Model model) {

        Job job = jobRepository.findById(id).orElse(null);

        model.addAttribute("job", job);

        return "edit-job";
    }

    @PostMapping("/update")
    public String updateJob(@ModelAttribute Job job) {

        jobRepository.save(job);

        return "redirect:/jobs";
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {

        jobRepository.deleteById(id);

        return "redirect:/jobs";
    }
}