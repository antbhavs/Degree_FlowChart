package com.course.degree.flowchart.controller;

import com.course.degree.flowchart.model.Course;
import com.course.degree.flowchart.model.Student;
import com.course.degree.flowchart.repository.CourseRepository;
import com.course.degree.flowchart.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/courses")
    public String showCourses(@RequestParam("email") String email, Model model) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Course> availableCourses = courseRepository.findByDegreeProgram(student.getDegree());

        Set<Course> enrolledCourses = student.getCourses();

        model.addAttribute("student", student);
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("enrolledCourses", enrolledCourses);

        return "courses";
    }
}
