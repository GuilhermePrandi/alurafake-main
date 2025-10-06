package br.com.alura.AluraFake.course.dto;

import br.com.alura.AluraFake.course.model.Status;

import java.time.LocalDateTime;

public record InstructorCourseReportDTO(Long id, String title, Status status, LocalDateTime publishedAt,
                                        Long taskCount) {
}
