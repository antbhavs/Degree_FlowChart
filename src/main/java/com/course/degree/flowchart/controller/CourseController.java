package com.course.degree.flowchart.controller;

import com.course.degree.flowchart.model.Course;
import com.course.degree.flowchart.model.Student;
import com.course.degree.flowchart.repository.CourseRepository;
import com.course.degree.flowchart.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.course.degree.flowchart.model.EnrollmentRequest;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/courses")
    public Object showCourses(@RequestParam("email") String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Course> availableCourses = courseRepository.findByDegreeProgram(student.getDegree());
        Set<Course> enrolledCourses = student.getCourses();

        return new Object() {
            public final Student studentInfo = student;
            public final List<Course> availableCoursesList = availableCourses;
            public final Set<Course> enrolledCoursesSet = enrolledCourses;
        };
    }

    @PostMapping("/courses/add")
    public Course addCourse(@RequestBody Course course) {
        if (course.getPrerequisite() != null) {
            Long prereqId = course.getPrerequisite().getId();
            Course prereq = courseRepository.findById(prereqId)
                    .orElseThrow(() -> new RuntimeException("Prerequisite course not found"));
            course.setPrerequisite(prereq);
        }
        return courseRepository.save(course);
    }

    @PostMapping("/courses/{courseId}/prerequisite/{prereqId}")
    public Course setPrerequisite(@PathVariable Long courseId, @PathVariable Long prereqId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Course prereq = courseRepository.findById(prereqId)
                .orElseThrow(() -> new RuntimeException("Prerequisite course not found"));
        course.setPrerequisite(prereq);
        return courseRepository.save(course);
    }

    @PostMapping("/students/enroll")
    public Student enrollStudent(@RequestBody EnrollmentRequest request) {
        Student student = studentRepository.findByEmail(request.getStudentEmail())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        student.getCourses().add(course);
        course.getStudents().add(student);

        studentRepository.save(student);
        courseRepository.save(course);

        return student;
    }
}
