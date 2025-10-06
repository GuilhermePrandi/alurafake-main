package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.course.model.Status;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.repository.TaskRepository;
import br.com.alura.AluraFake.task.validator.TaskValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TaskValidator taskValidator;

    @InjectMocks
    private TaskService taskService;

    private Course buildingCourse;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        buildingCourse = new Course();
        buildingCourse.setStatus(Status.BUILDING);

        task = new Task();
        task.setTaskOrder(1);
        task.setStatement("Test statement");
    }

    @Test
    void createTask_should_save_task_when_all_validations_pass() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskRepository.existsByCourseIdAndStatement(1L, task.getStatement())).thenReturn(false);
        when(taskRepository.findByCourseIdOrderByTaskOrder(1L)).thenReturn(new ArrayList<>());
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(1L, task);

        assertEquals(task, result);
        verify(taskValidator, times(1)).validateTask(task);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void createTask_should_throw_exception_when_course_not_found() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.createTask(1L, task));

        assertEquals("Curso não encontrado", exception.getMessage());
    }

    @Test
    void createTask_should_throw_exception_when_course_not_building() {
        buildingCourse.setStatus(Status.PUBLISHED);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> taskService.createTask(1L, task));

        assertEquals("Não é possível adicionar atividades em cursos publicados", exception.getMessage());
    }

    @Test
    void createTask_should_throw_exception_when_statement_already_exists() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskRepository.existsByCourseIdAndStatement(1L, task.getStatement())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.createTask(1L, task));

        assertEquals("Já existe uma atividade com esse enunciado no curso", exception.getMessage());
    }

    @Test
    void createTask_should_throw_exception_when_task_order_too_high() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskRepository.existsByCourseIdAndStatement(1L, task.getStatement())).thenReturn(false);

        List<Task> existingTasks = new ArrayList<>();
        Task t1 = new Task();
        t1.setTaskOrder(1);
        existingTasks.add(t1);

        when(taskRepository.findByCourseIdOrderByTaskOrder(1L)).thenReturn(existingTasks);

        task.setTaskOrder(5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.createTask(1L, task));

        assertEquals("A ordem da atividade não pode pular números", exception.getMessage());
    }

    @Test
    void createTask_should_adjust_task_order_of_existing_tasks() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskRepository.existsByCourseIdAndStatement(1L, task.getStatement())).thenReturn(false);

        Task t1 = new Task();
        t1.setTaskOrder(1);

        Task t2 = new Task();
        t2.setTaskOrder(2);

        List<Task> existingTasks = new ArrayList<>();
        existingTasks.add(t1);
        existingTasks.add(t2);

        when(taskRepository.findByCourseIdOrderByTaskOrder(1L)).thenReturn(existingTasks);
        when(taskRepository.save(task)).thenAnswer(invocation -> invocation.getArgument(0));

        task.setTaskOrder(2);

        Task result = taskService.createTask(1L, task);

        assertSame(task, result);

        assertEquals(1, existingTasks.get(0).getTaskOrder());
        assertEquals(3, existingTasks.get(1).getTaskOrder());
        assertEquals(2, result.getTaskOrder());

        verify(taskRepository, times(1)).saveAll(existingTasks);
        verify(taskRepository, times(1)).save(task);
    }

}
