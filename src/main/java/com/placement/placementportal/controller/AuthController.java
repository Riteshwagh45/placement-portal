package com.placement.placementportal.controller;

import com.placement.placementportal.entity.Student;
import com.placement.placementportal.repository.StudentRepository;
import com.placement.placementportal.entity.Company;
import com.placement.placementportal.repository.CompanyRepository;
import com.placement.placementportal.entity.User;
import com.placement.placementportal.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam(required = false) String location,
                               @RequestParam(required = false) String description,
                               Model model) {

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            model.addAttribute("error", "Email already registered!");
            model.addAttribute("user", user);
            return "signup";
        }

        userRepository.save(user);

        if ("COMPANY".equalsIgnoreCase(user.getRole())) {
            Company company = new Company();
            company.setCompanyName(user.getUsername());
            company.setEmail(user.getEmail());
            company.setLocation(location);
            company.setDescription(description);
            companyRepository.save(company);
        } else if ("STUDENT".equalsIgnoreCase(user.getRole())) {
            Student student = new Student();
            student.setName(user.getUsername());
            student.setEmail(user.getEmail());
            studentRepository.save(student);
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            HttpSession session,
            Model model) {

        User user = userRepository.findByEmailAndPassword(email, password);

        if (user != null) {
            if (!user.getRole().equalsIgnoreCase(role)) {
                model.addAttribute("error", "Invalid Role Selected");
                return "login";
            }

            session.setAttribute("loggedUser", user);

            if ("ADMIN".equalsIgnoreCase(role)) {
                return "redirect:/admin-dashboard";
            }

            if ("COMPANY".equalsIgnoreCase(role)) {
                return "redirect:/company-dashboard";
            }

            return "redirect:/student-dashboard";
        }

        model.addAttribute("error", "Invalid Email or Password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}