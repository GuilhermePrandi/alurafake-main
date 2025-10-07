package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.dto.InstructorCourseReportDTO;
import br.com.alura.AluraFake.course.dto.InstructorCourseReportResponseDTO;
import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.course.model.Status;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.repository.TaskRepository;
import br.com.alura.AluraFake.course.validator.CourseValidator;
import br.com.alura.AluraFake.user.model.Role;
import br.com.alura.AluraFake.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;
    private final CourseValidator courseValidator;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository,
                         TaskRepository taskRepository,
                         CourseValidator courseValidator, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
        this.courseValidator = courseValidator;
        this.userRepository = userRepository;
    }

    @Transactional
    public Course publishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado"));

        List<Task> tasks = taskRepository.findByCourseIdOrderByTaskOrder(courseId);

        courseValidator.validateBeforePublish(course, tasks);

        course.setStatus(Status.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }

    public InstructorCourseReportResponseDTO getCoursesByInstructor(Long instructorId) {
        var instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instrutor não encontrado"));

        if (!instructor.getRole().equals(Role.INSTRUCTOR)) {
            throw new IllegalStateException("Usuário não é um instrutor");
        }

        List<InstructorCourseReportDTO> courses =
                courseRepository.findCoursesByInstructorId(instructorId);

        long totalCourses = courses.size();

        return new InstructorCourseReportResponseDTO(totalCourses, courses);
    }
}
