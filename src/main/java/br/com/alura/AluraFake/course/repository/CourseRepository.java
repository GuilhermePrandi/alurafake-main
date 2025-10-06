package br.com.alura.AluraFake.course.repository;

import br.com.alura.AluraFake.course.dto.InstructorCourseReportDTO;
import br.com.alura.AluraFake.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT new br.com.alura.AluraFake.course.dto.InstructorCourseReportDTO(c.id, c.title, c.status, c.publishedAt, COUNT(t)) " +
            "FROM Course c LEFT JOIN Task t ON t.course.id = c.id " +
            "WHERE c.instructor.id = :instructorId " +
            "GROUP BY c.id")
    List<InstructorCourseReportDTO> findCoursesByInstructorId(@Param("instructorId") Long instructorId);
}
