package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.model.Status;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.model.TaskType;
import br.com.alura.AluraFake.user.model.Role;
import br.com.alura.AluraFake.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseValidatorTest {

    private CourseValidator courseValidator;
    private User instructor;
    private Course course;
    private List<Task> tasks;

    @BeforeEach
    void setUp() {
        courseValidator = new CourseValidator();
        instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        course = new Course("Java", "Curso de Java", instructor);

        tasks = new ArrayList<>();

        Task t1 = new Task();
        t1.setId(1L);
        t1.setTaskOrder(1);
        t1.setType(TaskType.OPEN_TEXT);

        Task t2 = new Task();
        t2.setId(2L);
        t2.setTaskOrder(2);
        t2.setType(TaskType.SINGLE_CHOICE);

        Task t3 = new Task();
        t3.setId(3L);
        t3.setTaskOrder(3);
        t3.setType(TaskType.MULTIPLE_CHOICE);

        tasks.add(t1);
        tasks.add(t2);
        tasks.add(t3);
    }

    @Test
    void validateBeforePublish_should_pass_when_all_rules_are_ok() {
        assertDoesNotThrow(() -> courseValidator.validateBeforePublish(course, tasks));
    }

    @Test
    void validateBeforePublish_should_throw_if_course_not_building() {
        course.setStatus(Status.PUBLISHED);
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> courseValidator.validateBeforePublish(course, tasks));
        assertEquals("Somente cursos em BUILDING podem ser publicados", ex.getMessage());
    }

    @Test
    void validateBeforePublish_should_throw_if_task_types_missing() {
        tasks.removeIf(t -> t.getType() == TaskType.OPEN_TEXT);
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> courseValidator.validateBeforePublish(course, tasks));
        assertEquals("Curso deve ter ao menos uma atividade de cada tipo", ex.getMessage());
    }

    @Test
    void validateBeforePublish_should_throw_if_task_order_not_continuous() {
        tasks.get(1).setTaskOrder(5);
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> courseValidator.validateBeforePublish(course, tasks));
        assertTrue(ex.getMessage().contains("tem ordem incorreta"));
    }
}
