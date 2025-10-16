package com.course.degree.flowchart.model;

public class EnrollmentRequest {
    private String studentEmail;
    private Long courseId;

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
}
