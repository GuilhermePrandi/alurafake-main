package br.com.alura.AluraFake.infra;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.model.TaskOption;
import br.com.alura.AluraFake.task.model.TaskType;
import br.com.alura.AluraFake.task.repository.TaskRepository;
import br.com.alura.AluraFake.user.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;

    public DataSeeder(UserRepository userRepository, CourseRepository courseRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) {
        if (!"dev".equals(activeProfile)) return;

        if (userRepository.count() == 0) {
            User caio = new User("Caio", "caio@alura.com.br", Role.STUDENT);
            User paulo = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
            userRepository.saveAll(Arrays.asList(caio, paulo));

            Course java = courseRepository.save(new Course("Java", "Aprenda Java com Alura", paulo));
            courseRepository.save(new Course("Python", "Aprenda Python com Alura", paulo));
            courseRepository.save(new Course("Kotlin", "Aprenda Kotlin com Alura", paulo));

            Task t1 = new Task();
            t1.setStatement("Instale o JDK 18");
            t1.setType(TaskType.OPEN_TEXT);
            t1.setTaskOrder(1);
            t1.setCourse(java);
            t1.setCreatedAt(LocalDateTime.now());

            taskRepository.save(t1);

            Task t2 = new Task();
            t2.setStatement("Crie sua primeira classe Java");
            t2.setType(TaskType.SINGLE_CHOICE);
            t2.setTaskOrder(2);
            t2.setCourse(java);
            t2.setCreatedAt(LocalDateTime.now());

            TaskOption o1 = new TaskOption(t2, "Alternativa A", false);
            TaskOption o2 = new TaskOption(t2, "Alternativa B", true);
            TaskOption o3 = new TaskOption(t2, "Alternativa C", false);

            t2.setOptions(Arrays.asList(o1, o2, o3));

            taskRepository.save(t2);

            Task t3 = new Task();
            t3.setStatement("Escolha a alternativa correta sobre JVM");
            t3.setType(TaskType.MULTIPLE_CHOICE);
            t3.setTaskOrder(3);
            t3.setCourse(java);
            t3.setCreatedAt(LocalDateTime.now());

            TaskOption t3o1 = new TaskOption(t3, "Alternativa A", false);
            TaskOption t3o2 = new TaskOption(t3, "Alternativa B", true);
            TaskOption t3o3 = new TaskOption(t3, "Alternativa C", false);
            TaskOption t3o4 = new TaskOption(t3, "Alternativa D", true);

            t3.setOptions(Arrays.asList(t3o1, t3o2, t3o3, t3o4));

            taskRepository.save(t3);
        }
    }
}