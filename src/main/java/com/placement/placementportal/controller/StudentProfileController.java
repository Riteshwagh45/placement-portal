package com.placement.placementportal.controller;

import com.placement.placementportal.entity.Student;
import com.placement.placementportal.entity.User;
import com.placement.placementportal.repository.StudentRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class StudentProfileController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/student-profile")
    public String studentProfile(
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

            student = new Student();

            student.setEmail(
                    user.getEmail());

            studentRepository.save(
                    student);
        }

        model.addAttribute(
                "student",
                student);

        return "student-profile";
    }

    @PostMapping("/student-profile")
    public String updateProfile(
            @ModelAttribute Student student,
            @RequestParam("resumeFile")
            MultipartFile resumeFile) {

        try {

            if (!resumeFile.isEmpty()) {

                String uploadDir =
                        "uploads/";

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

                resumeFile.transferTo(
                        destination);

                student.setResumeUrl(
                        uploadDir + fileName);
            }

            studentRepository.save(student);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return "redirect:/student-profile?success=true";
    }
}