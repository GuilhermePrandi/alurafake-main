package br.com.alura.AluraFake.course.dto;

import java.util.List;

public record InstructorCourseReportResponseDTO(Long totalCourses, List<InstructorCourseReportDTO> courses) {
}
