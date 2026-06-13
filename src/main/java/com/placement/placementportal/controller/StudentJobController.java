package com.placement.placementportal.controller;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import com.placement.placementportal.entity.Application;
import com.placement.placementportal.entity.Job;
import com.placement.placementportal.entity.Student;
import com.placement.placementportal.entity.User;
import com.placement.placementportal.repository.ApplicationRepository;
import com.placement.placementportal.repository.JobRepository;
import com.placement.placementportal.repository.StudentRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StudentJobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/student-jobs")
    public String viewJobs(
            HttpSession session,
            Model model,
            @RequestParam(required = false)
            String alreadyApplied) {

        User user =
                (User) session.getAttribute(
                        "loggedUser");

        if (user == null) {

            return "redirect:/login";
        }

        List<Job> jobs =
                jobRepository.findAll();

        model.addAttribute(
                "jobs",
                jobs);

        if (alreadyApplied != null) {

            model.addAttribute(
                    "message",
                    "You have already applied for this job!");
        }

        return "student-jobs";
    }

    @GetMapping("/apply-job/{jobId}")
    public String applyJob(
            @PathVariable Long jobId,
            HttpSession session) {

        User user =
                (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        Student student =
                studentRepository.findByEmail(user.getEmail());

        if (student == null) {
            return "redirect:/student-jobs";
        }

        Application existingApplication =
                applicationRepository.findByStudentIdAndJobId(
                        student.getId(),
                        jobId);

        if (existingApplication != null) {
            return "redirect:/student-jobs?alreadyApplied=true";
        }

        Application app = new Application();

        app.setStudentId(student.getId());
        app.setJobId(jobId);
        app.setStatus("APPLIED");

        applicationRepository.save(app);

        return "redirect:/student-jobs";
    }

    @GetMapping("/my-applications")
    public String myApplications(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute(
                        "loggedUser");

        if (user == null) {

            return "redirect:/login";
        }

        Student student =
                studentRepository.findByEmail(
                        user.getEmail());

        if (student == null) {

            return "redirect:/student-dashboard";
        }

        List<Application> applications =
                applicationRepository.findByStudentId(
                        student.getId());

        model.addAttribute(
                "applications",
                applications);

        model.addAttribute(
                "jobs",
                jobRepository.findAll());

        return "my-applications";
    }
    @GetMapping("/manual-apply/{jobId}")
    public String showManualApplyForm(
            @PathVariable Long jobId,
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute(
                        "loggedUser");

        if (user == null) {

            return "redirect:/login";
        }

        Student student =
                studentRepository.findByEmail(
                        user.getEmail());

        Job job =
                jobRepository.findById(jobId)
                        .orElse(null);

        if (student == null || job == null) {

            return "redirect:/student-jobs";
        }

        Application application =
                new Application();

        application.setJobId(
                jobId);

        model.addAttribute(
                "job",
                job);

        model.addAttribute(
                "student",
                student);

        model.addAttribute(
                "application",
                application);

        return "manual-apply";
    }

    @PostMapping("/manual-apply")
    public String submitManualApply(
            @ModelAttribute Application application,
            @RequestParam("resumeFile") MultipartFile resumeFile,
            HttpSession session) {

        User user =
                (User) session.getAttribute(
                        "loggedUser");

        if (user == null) {

            return "redirect:/login";
        }

        Student student =
                studentRepository.findByEmail(
                        user.getEmail());

        if (student == null) {

            return "redirect:/student-jobs";
        }

        Application existingApplication =
                applicationRepository.findByStudentIdAndJobId(
                        student.getId(),
                        application.getJobId());

        if (existingApplication != null) {

            return "redirect:/student-jobs?alreadyApplied=true";
        }

        try {

            if (!resumeFile.isEmpty()) {

                String uploadDir =
                        System.getProperty("user.dir")
                                + File.separator
                                + "uploads"
                                + File.separator;

                File dir =
                        new File(uploadDir);

                if (!dir.exists()) {

                    dir.mkdirs();
                }

                String fileName =
                        System.currentTimeMillis()
                                + "_"
                                + resumeFile.getOriginalFilename();

                File destination =
                        new File(uploadDir + fileName);

                resumeFile.transferTo(destination);

                application.setResumeUrl(
                        "uploads/" + fileName);
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        application.setStudentId(
                student.getId());

        application.setStatus(
                "APPLIED");

        application.setApplicationType(
                "MANUAL_APPLY");

        applicationRepository.save(
                application);

        return "redirect:/my-applications";
    }

}