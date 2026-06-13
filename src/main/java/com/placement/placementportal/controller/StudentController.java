package com.placement.placementportal.controller;

import com.placement.placementportal.entity.Application;
import com.placement.placementportal.entity.Student;
import com.placement.placementportal.entity.User;
import com.placement.placementportal.repository.ApplicationRepository;
import com.placement.placementportal.repository.StudentRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @GetMapping("/student-dashboard")
    public String studentDashboard(
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

            return "redirect:/login";
        }

        List<Application> applications =
                applicationRepository.findByStudentId(
                        student.getId());

        model.addAttribute(
                "student",
                student);

        model.addAttribute(
                "applications",
                applications);

        return "student-dashboard";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudent(
            @PathVariable Long id,
            Model model) {

        Student student =
                studentRepository.findById(id)
                        .orElse(null);

        if (student == null) {

            return "redirect:/students";
        }

        model.addAttribute(
                "student",
                student);

        return "edit-student";
    }

    @PostMapping("/students/update")
    public String updateStudent(
            @ModelAttribute Student student) {

        studentRepository.save(student);

        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(
            @PathVariable Long id) {

        studentRepository.deleteById(id);

        return "redirect:/students";
    }
    @GetMapping("/students/add")
    public String showAddStudentForm(Model model) {

        model.addAttribute(
                "student",
                new Student());

        return "add-student";
    }

    @PostMapping("/students/save")
    public String saveStudent(
            @ModelAttribute Student student) {

        studentRepository.save(student);

        return "redirect:/students";
    }
}