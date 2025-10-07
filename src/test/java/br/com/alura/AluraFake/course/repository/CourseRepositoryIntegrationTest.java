package br.com.alura.AluraFake.course.repository;

import br.com.alura.AluraFake.course.dto.InstructorCourseReportDTO;
import br.com.alura.AluraFake.course.model.Course;
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
class CourseRepositoryIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private br.com.alura.AluraFake.task.repository.TaskRepository taskRepository;

    @Test
    void findCoursesByInstructorId__should_return_courses_with_task_count() {
        User instructor = new User("Guilherme", "gui@alura.com.br", Role.INSTRUCTOR);
        userRepository.save(instructor);

        Course javaCourse = new Course("Java Básico", "Introdução à linguagem Java", instructor);
        javaCourse.setPublishedAt(LocalDateTime.now());
        courseRepository.save(javaCourse);

        Course springCourse = new Course("Spring Boot Avançado", "Criação de APIs com Spring Boot", instructor);
        springCourse.setPublishedAt(LocalDateTime.now());
        courseRepository.save(springCourse);

        Task task1 = new Task();
        task1.setStatement("Variáveis e Tipos");
        task1.setType(TaskType.OPEN_TEXT);
        task1.setTaskOrder(1);
        task1.setCourse(javaCourse);
        task1.setCreatedAt(LocalDateTime.now());

        Task task2 = new Task();
        task2.setStatement("Laços de Repetição");
        task2.setType(TaskType.OPEN_TEXT);
        task2.setTaskOrder(2);
        task2.setCourse(javaCourse);
        task2.setCreatedAt(LocalDateTime.now());

        Task task3 = new Task();
        task3.setStatement("Configuração de Beans");
        task3.setType(TaskType.OPEN_TEXT);
        task3.setTaskOrder(1);
        task3.setCourse(springCourse);
        task3.setCreatedAt(LocalDateTime.now());

        taskRepository.saveAll(List.of(task1, task2, task3));

        List<InstructorCourseReportDTO> report = courseRepository.findCoursesByInstructorId(instructor.getId());

        assertThat(report).hasSize(2);
        InstructorCourseReportDTO javaReport = report.stream()
                .filter(r -> r.title().equals("Java Básico"))
                .findFirst()
                .orElseThrow();
        InstructorCourseReportDTO springReport = report.stream()
                .filter(r -> r.title().equals("Spring Boot Avançado"))
                .findFirst()
                .orElseThrow();
        assertThat(javaReport.taskCount()).isEqualTo(2);
        assertThat(springReport.taskCount()).isEqualTo(1);
    }

    @Test
    void findCoursesByInstructorId__should_return_empty_when_instructor_has_no_courses() {
        User instructor = new User("Marina", "marina@alura.com.br", Role.INSTRUCTOR);
        userRepository.save(instructor);

        List<InstructorCourseReportDTO> report = courseRepository.findCoursesByInstructorId(instructor.getId());

        assertThat(report).isEmpty();
    }
}
