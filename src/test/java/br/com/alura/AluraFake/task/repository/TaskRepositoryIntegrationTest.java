package br.com.alura.AluraFake.task.repository;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.model.TaskType;
import br.com.alura.AluraFake.user.model.Role;
import br.com.alura.AluraFake.user.model.User;
import br.com.alura.AluraFake.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByCourseIdOrderByTaskOrder__should_return_tasks_sorted_by_order() {
        User instructor = userRepository.save(new User("Marina", "marina@alura.com.br", Role.INSTRUCTOR));
        Course course = courseRepository.save(new Course("Spring Boot", "Aprenda Spring Boot com testes", instructor));

        Task task1 = new Task();
        task1.setCourse(course);
        task1.setStatement("Introdução");
        task1.setTaskOrder(2);
        task1.setType(TaskType.MULTIPLE_CHOICE);
        task1.setCreatedAt(LocalDateTime.now());

        Task task2 = new Task();
        task2.setCourse(course);
        task2.setStatement("Configuração de Projeto");
        task2.setTaskOrder(1);
        task2.setType(TaskType.MULTIPLE_CHOICE);
        task2.setCreatedAt(LocalDateTime.now());

        taskRepository.saveAll(List.of(task1, task2));

        List<Task> result = taskRepository.findByCourseIdOrderByTaskOrder(course.getId());

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStatement()).isEqualTo("Configuração de Projeto");
        assertThat(result.get(1).getStatement()).isEqualTo("Introdução");
    }

    @Test
    void existsByCourseIdAndStatement__should_return_true_when_task_exists() {
        User instructor = userRepository.save(new User("Leonardo", "leo@alura.com.br", Role.INSTRUCTOR));
        Course course = courseRepository.save(new Course("Java Avançado", "Dominando Java", instructor));

        Task task = new Task();
        task.setCourse(course);
        task.setStatement("Polimorfismo");
        task.setTaskOrder(1);
        task.setType(TaskType.MULTIPLE_CHOICE);
        task.setCreatedAt(LocalDateTime.now());

        taskRepository.save(task);

        assertThat(taskRepository.existsByCourseIdAndStatement(course.getId(), "Polimorfismo")).isTrue();
        assertThat(taskRepository.existsByCourseIdAndStatement(course.getId(), "Herança")).isFalse();
    }
}
