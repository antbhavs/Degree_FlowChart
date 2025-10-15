package com.course.degree.flowchart.controller;

import com.course.degree.flowchart.model.Course;
import com.course.degree.flowchart.model.Student;
import com.course.degree.flowchart.repository.CourseRepository;
import com.course.degree.flowchart.repository.StudentRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CourseRepository courseRepository;

    private final StudentRepository repo;

    public AuthController(StudentRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("student", new Student());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("student") Student formStudent, Model model, HttpSession session) {
        Optional<Student> studentOpt = repo.findByEmail(formStudent.getEmail());
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (student.getPassword().equals(formStudent.getPassword())) {
                session.setAttribute("loggedInStudent", student);

                model.addAttribute("name", student.getName());

                model.addAttribute("enrolledCourses", student.getCourses());

                List<Course> availableCourses = courseRepository.findByDegreeProgram(student.getDegree());
                model.addAttribute("availableCourses", availableCourses);

                return "welcome";
            }
        }
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("student") Student newStudent, Model model) {
        Optional<Student> existing = repo.findByEmail(newStudent.getEmail());
        if (existing.isPresent()) {
            model.addAttribute("error", "Email already registered. Please login instead.");
            return "register";
        }

        repo.save(newStudent);

        model.addAttribute("message", "Registration successful! Please login.");
        model.addAttribute("student", new Student());
        return "login";
    }
}
