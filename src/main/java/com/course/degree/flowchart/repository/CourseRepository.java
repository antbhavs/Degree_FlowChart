package com.course.degree.flowchart.repository;

import com.course.degree.flowchart.model.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByDegreeProgram(String degreeProgram);
}
