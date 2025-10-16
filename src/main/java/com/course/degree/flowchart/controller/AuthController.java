package com.course.degree.flowchart.controller;

import com.course.degree.flowchart.model.Course;
import com.course.degree.flowchart.model.Student;
import com.course.degree.flowchart.repository.CourseRepository;
import com.course.degree.flowchart.repository.StudentRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("student", new Student());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("student") Student formStudent, Model model) {
        Student existingStudent = studentRepository.findByEmail(formStudent.getEmail());

        if (existingStudent == null ||
                !existingStudent.getPassword().equals(formStudent.getPassword())) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        List<Course> allCourses = courseRepository.findByDegreeProgram(existingStudent.getDegree());

        model.addAttribute("student", existingStudent);
        model.addAttribute("enrolledCourses", existingStudent.getCourses());
        model.addAttribute("allCourses", allCourses);

        return "welcome";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth/login";
    }
}
