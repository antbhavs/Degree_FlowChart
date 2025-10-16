package com.course.degree.flowchart.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CourseGraphResponse {
    private List<Object> nodes;
    private List<Object> edges;

}

