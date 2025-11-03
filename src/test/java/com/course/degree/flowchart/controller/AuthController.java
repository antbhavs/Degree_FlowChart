package com.course.degree.flowchart.controller;

import com.course.degree.flowchart.model.Course;
import com.course.degree.flowchart.model.Student;
import com.course.degree.flowchart.repository.CourseRepository;
import com.course.degree.flowchart.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

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

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

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
        student.setPassword("hashed123");
        student.setDegree("CS");
        student.setYear(2);

        Course course = new Course();
        course.setName("Test Course");

        when(studentRepository.findByEmail("test@example.com")).thenReturn(student);
        when(passwordEncoder.matches("123", "hashed123")).thenReturn(true);
        when(courseRepository.findByDegreeProgram("CS")).thenReturn(List.of(course));

        Student formStudent = new Student();
        formStudent.setEmail("test@example.com");
        formStudent.setPassword("123");

        String result = authController.processLogin(formStudent, model);

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
        when(passwordEncoder.matches("wrong", "1234")).thenReturn(false);

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

    @Test
    void testShowRegistrationForm_ShouldReturnRegisterPage() {
        String view = authController.showRegistrationForm(model);
        verify(model).addAttribute(eq("student"), any(Student.class));
        assertEquals("register", view);
    }

    @Test
    void testProcessRegistration_Success() {
        Student newStudent = new Student();
        newStudent.setEmail("new@example.com");
        newStudent.setPassword("pass");

        when(studentRepository.findByEmail("new@example.com")).thenReturn(null);
        when(passwordEncoder.encode("pass")).thenReturn("hashedPass");

        RedirectAttributes redirectAttrs = new RedirectAttributesModelMap();

        String view = authController.processRegistration(newStudent, redirectAttrs);

        verify(studentRepository).save(any(Student.class));
        assertEquals("redirect:/auth/login", view);
        assertEquals("Registration successful. Please login.", redirectAttrs.getFlashAttributes().get("success"));
    }

    @Test
    void testProcessRegistration_EmailExists_ShouldRedirectBackWithError() {
        Student newStudent = new Student();
        newStudent.setEmail("john@example.com");

        when(studentRepository.findByEmail("john@example.com")).thenReturn(student);

        RedirectAttributes redirectAttrs = new RedirectAttributesModelMap();

        String view = authController.processRegistration(newStudent, redirectAttrs);

        verify(studentRepository, never()).save(any());
        assertEquals("redirect:/auth/register", view);
        assertEquals("Email already exists", redirectAttrs.getFlashAttributes().get("error"));
    }
}
