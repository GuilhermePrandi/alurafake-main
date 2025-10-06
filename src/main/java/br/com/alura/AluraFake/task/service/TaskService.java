package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.model.Status;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.task.repository.TaskRepository;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.validator.TaskValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CourseRepository courseRepository;
    private final TaskValidator taskValidator;

    public TaskService(TaskRepository taskRepository, CourseRepository courseRepository, TaskValidator taskValidator) {
        this.taskRepository = taskRepository;
        this.courseRepository = courseRepository;
        this.taskValidator = taskValidator;
    }

    @Transactional
    public Task createTask(Long courseId, Task task) {
        Course course = findCourseOrThrow(courseId);

        validateCourseBuilding(course);

        task.setCourse(course);

        taskValidator.validateTask(task);

        validateUniqueStatement(courseId, task.getStatement());

        adjustTaskOrder(courseId, task.getTaskOrder());

        return taskRepository.save(task);
    }

    private Course findCourseOrThrow(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado"));
    }

    private void validateCourseBuilding(Course course) {
        if (course.getStatus() != Status.BUILDING) {
            throw new IllegalStateException("Não é possível adicionar atividades em cursos publicados");
        }
    }

    private void validateUniqueStatement(Long courseId, String statement) {
        if (taskRepository.existsByCourseIdAndStatement(courseId, statement)) {
            throw new IllegalArgumentException("Já existe uma atividade com esse enunciado no curso");
        }
    }

    private void adjustTaskOrder(Long courseId, int newOrder) {
        List<Task> tasks = taskRepository.findByCourseIdOrderByTaskOrder(courseId);
        if (newOrder > tasks.size() + 1) {
            throw new IllegalArgumentException("A ordem da atividade não pode pular números");
        }
        tasks.stream()
                .filter(t -> t.getTaskOrder() >= newOrder)
                .forEach(t -> t.setTaskOrder(t.getTaskOrder() + 1));
        taskRepository.saveAll(tasks);
    }
}
