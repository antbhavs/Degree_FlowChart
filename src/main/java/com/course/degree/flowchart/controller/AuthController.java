package com.course.degree.flowchart.controller;

import com.course.degree.flowchart.model.Course;
import com.course.degree.flowchart.model.Student;
import com.course.degree.flowchart.repository.CourseRepository;
import com.course.degree.flowchart.repository.StudentRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("student", new Student());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("student") Student formStudent, Model model) {
        Student existingStudent = studentRepository.findByEmail(formStudent.getEmail());

        if (existingStudent == null ||
                !passwordEncoder.matches(formStudent.getPassword(), existingStudent.getPassword())) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        List<Course> allCourses = courseRepository.findByDegreeProgram(existingStudent.getDegree());
        int currentSemester = existingStudent.getYear() * 2;
        if (currentSemester > 8) currentSemester = 8;

        model.addAttribute("student", existingStudent);
        model.addAttribute("enrolledCourses", existingStudent.getCourses());
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("currentSemester", currentSemester);

        return "welcome";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth/login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("student", new Student());
        return "register"; 
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("student") Student newStudent,
                                      RedirectAttributes redirectAttrs) {
        if (studentRepository.findByEmail(newStudent.getEmail()) != null) {
            redirectAttrs.addFlashAttribute("error", "Email already exists");
            return "redirect:/auth/register";
        }

        newStudent.setPassword(passwordEncoder.encode(newStudent.getPassword()));

        studentRepository.save(newStudent);

        redirectAttrs.addFlashAttribute("success", "Registration successful. Please login.");
        return "redirect:/auth/login";
    }
}
