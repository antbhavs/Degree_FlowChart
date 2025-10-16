package com.course.degree.flowchart.controller;

import com.course.degree.flowchart.model.Course;
import com.course.degree.flowchart.model.Student;
import com.course.degree.flowchart.model.CourseGraphResponse;
import com.course.degree.flowchart.model.EnrollmentRequest;
import com.course.degree.flowchart.repository.CourseRepository;
import com.course.degree.flowchart.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/courses/graph")
    public CourseGraphResponse getCourseGraph(@RequestParam String email) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) throw new RuntimeException("Student not found");

        List<Course> courses = courseRepository.findByDegreeProgram(student.getDegree());

        List<Object> nodes = new ArrayList<>();
        List<Object> edges = new ArrayList<>();

        for (Course course : courses) {
            boolean enrolled = student.getCourses().contains(course);
            nodes.add(Map.of(
                "data", Map.of(
                    "id", "C" + course.getId(),
                    "label", course.getCode() + "\n" + course.getName(),
                    "enrolled", enrolled
                )
            ));

            if (course.getPrerequisite() != null) {
                edges.add(Map.of(
                    "data", Map.of(
                        "source", "C" + course.getPrerequisite().getId(),
                        "target", "C" + course.getId()
                    )
                ));
            }
        }

        return new CourseGraphResponse(nodes, edges);
    }

    @PostMapping("/students/enroll")
    public Student enrollStudent(@RequestBody EnrollmentRequest request) {
        Student student = studentRepository.findByEmail(request.getStudentEmail());
        if (student == null) throw new RuntimeException("Student not found");

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        student.getCourses().add(course);
        course.getStudents().add(student);

        studentRepository.save(student);
        courseRepository.save(course);

        return student;
    }
}
