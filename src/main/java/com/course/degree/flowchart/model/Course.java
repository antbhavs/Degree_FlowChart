package com.course.degree.flowchart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String degreeProgram;

    @ManyToOne
    private Course prerequisite;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}
