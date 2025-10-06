package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.course.model.Status;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.repository.TaskRepository;
import br.com.alura.AluraFake.course.validator.CourseValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;
    private final CourseValidator courseValidator;

    public CourseService(CourseRepository courseRepository,
                         TaskRepository taskRepository,
                         CourseValidator courseValidator) {
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
        this.courseValidator = courseValidator;
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
}
