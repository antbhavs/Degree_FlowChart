package com.course.degree.flowchart.controller;

import com.course.degree.flowchart.model.Course;
import com.course.degree.flowchart.model.Student;
import com.course.degree.flowchart.repository.CourseRepository;
import com.course.degree.flowchart.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private Model model;

    @InjectMocks
    private AuthController authController;

    private Student student;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        student = new Student();
        student.setEmail("john@example.com");
        student.setPassword("1234");
        student.setName("John");
        student.setDegree("B.Tech");
        student.setYear(2);
        student.setCourses(Set.of());
    }

    @Test
    void testShowLoginForm_ShouldReturnLoginPage() {
        String view = authController.showLoginForm(model);
        verify(model).addAttribute(eq("student"), any(Student.class));
        assertEquals("login", view);
    }

    @Test
    void testProcessLogin_SuccessfulLogin() {
        Student student = new Student();
        student.setEmail("test@example.com");
        student.setPassword("123");
        student.setDegree("CS");
        student.setYear(2);

        Course course = new Course();
        course.setName("Test Course");

        when(studentRepository.findByEmail("test@example.com")).thenReturn(student);
        when(courseRepository.findByDegreeProgram("CS")).thenReturn(List.of(course));

        String result = authController.processLogin(student, model);

        assertEquals("welcome", result);
        verify(model).addAttribute(eq("student"), eq(student));
        verify(model).addAttribute(eq("enrolledCourses"), eq(student.getCourses()));
        verify(model).addAttribute(eq("allCourses"), anyList());
        verify(model).addAttribute(eq("currentSemester"), eq(4));
    }

    @Test
    void testProcessLogin_InvalidEmail_ShouldReturnLoginPageWithError() {
        when(studentRepository.findByEmail("invalid@example.com")).thenReturn(null);

        Student formStudent = new Student();
        formStudent.setEmail("invalid@example.com");
        formStudent.setPassword("wrong");

        String view = authController.processLogin(formStudent, model);

        verify(model).addAttribute("error", "Invalid email or password");
        assertEquals("login", view);
    }

    @Test
    void testProcessLogin_InvalidPassword_ShouldReturnLoginPageWithError() {
        when(studentRepository.findByEmail("john@example.com")).thenReturn(student);

        Student formStudent = new Student();
        formStudent.setEmail("john@example.com");
        formStudent.setPassword("wrong");

        String view = authController.processLogin(formStudent, model);

        verify(model).addAttribute("error", "Invalid email or password");
        assertEquals("login", view);
    }

    @Test
    void testLogout_ShouldRedirectToLogin() {
        String view = authController.logout();
        assertEquals("redirect:/auth/login", view);
    }
}
