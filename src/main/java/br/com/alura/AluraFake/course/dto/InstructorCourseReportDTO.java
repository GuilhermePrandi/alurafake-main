package br.com.alura.AluraFake.course.dto;

import br.com.alura.AluraFake.course.model.Status;

import java.time.LocalDateTime;

public class InstructorCourseReportDTO {
    private final Long id;
    private final String title;
    private final Status status;
    private final LocalDateTime publishedAt;
    private final Long taskCount;

    public InstructorCourseReportDTO(Long id, String title, Status status, LocalDateTime publishedAt, Long taskCount) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.publishedAt = publishedAt;
        this.taskCount = taskCount;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public Long getTaskCount() {
        return taskCount;
    }
}
