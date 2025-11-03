package com.course.degree.flowchart.controller;

import com.course.degree.flowchart.model.Course;
import com.course.degree.flowchart.model.Student;
import com.course.degree.flowchart.model.CourseGraphResponse;
import com.course.degree.flowchart.model.EnrollmentRequest;
import com.course.degree.flowchart.repository.CourseRepository;
import com.course.degree.flowchart.repository.StudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseControllerTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseController courseController;

    private Student student;
    private Course c1, c2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        c1 = new Course(1L, "CSE101", "Intro to CS", "B.Tech", null, new HashSet<>(),true);
        c2 = new Course(2L, "CSE201", "Data Structures", "B.Tech", c1, new HashSet<>(),true);

        student = new Student();
        student.setEmail("john@example.com");
        student.setDegree("B.Tech");
        student.setCourses(new HashSet<>(List.of(c1)));
    }

    @Test
    void testGetCourseGraph_Success() {
        when(studentRepository.findByEmail("john@example.com")).thenReturn(student);
        when(courseRepository.findByDegreeProgram("B.Tech")).thenReturn(List.of(c1, c2));

        CourseGraphResponse response = courseController.getCourseGraph("john@example.com");

        assertNotNull(response);
        assertEquals(2, response.getNodes().size());
        assertEquals(1, response.getEdges().size());
    }

    @Test
    void testEnrollStudent_Success() {
        EnrollmentRequest req = new EnrollmentRequest("john@example.com", 2L);
        when(studentRepository.findByEmail("john@example.com")).thenReturn(student);
        when(courseRepository.findById(2L)).thenReturn(Optional.of(c2));

        Student result = courseController.enrollStudent(req);

        assertTrue(result.getCourses().contains(c2));
        verify(studentRepository).save(student);
        verify(courseRepository).save(c2);
    }

    @Test
    void testGetEligibleCourses_Success() {
        when(studentRepository.findByEmail("john@example.com")).thenReturn(student);
        when(courseRepository.findByDegreeProgram("B.Tech")).thenReturn(List.of(c1, c2));

        List<Course> eligible = courseController.getEligibleCourses("john@example.com");

        assertEquals(1, eligible.size());
        assertTrue(eligible.contains(c2));
    }

    @Test
    void testGetSemesterGraph_Success() {
        when(studentRepository.findByEmail("john@example.com")).thenReturn(student);
        when(courseRepository.findByDegreeProgram("B.Tech")).thenReturn(List.of(c1, c2));

        CourseGraphResponse response = courseController.getSemesterGraph("john@example.com");

        assertNotNull(response);
        assertEquals(2, response.getNodes().size());
        assertEquals(1, response.getEdges().size());

        Map<?, ?> nodeData = (Map<?, ?>) ((Map<?, ?>) response.getNodes().get(0)).get("data");
        assertTrue(nodeData.containsKey("status"));
    }

    @Test
    void testDiscardPlannedCourses_Success() {
        when(studentRepository.findByEmail("john@example.com")).thenReturn(student);

        List<Long> removeIds = List.of(1L);
        Student updated = courseController.discardPlannedCourses("john@example.com", removeIds);

        assertFalse(updated.getCourses().contains(c1));
        verify(studentRepository).save(student);
    }

    @Test
    void testGetCourseGraph_ThrowsIfStudentNotFound() {
        when(studentRepository.findByEmail("missing@example.com")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> courseController.getCourseGraph("missing@example.com"));
    }

    @Test
    void testEnrollStudent_ThrowsIfCourseNotFound() {
        EnrollmentRequest req = new EnrollmentRequest("john@example.com", 999L);
        when(studentRepository.findByEmail("john@example.com")).thenReturn(student);
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> courseController.enrollStudent(req));
    }
}
