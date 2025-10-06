package br.com.alura.AluraFake.course.dto;

import java.util.List;

public class InstructorCourseReportResponseDTO {
    private final Long totalCourses;
    private final List<InstructorCourseReportDTO> courses;

    public InstructorCourseReportResponseDTO(Long totalCourses, List<InstructorCourseReportDTO> courses) {
        this.totalCourses = totalCourses;
        this.courses = courses;
    }

    public Long getTotalCourses() {
        return totalCourses;
    }

    public List<InstructorCourseReportDTO> getCourses() {
        return courses;
    }
}
