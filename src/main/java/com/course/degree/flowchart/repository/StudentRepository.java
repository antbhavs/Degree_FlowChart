package com.course.degree.flowchart.repository;

import com.course.degree.flowchart.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByEmail(String email);
}
