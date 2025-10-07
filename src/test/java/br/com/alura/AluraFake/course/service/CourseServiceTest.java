package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.dto.InstructorCourseReportDTO;
import br.com.alura.AluraFake.course.dto.InstructorCourseReportResponseDTO;
import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.model.Status;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.model.TaskType;
import br.com.alura.AluraFake.task.repository.TaskRepository;
import br.com.alura.AluraFake.course.validator.CourseValidator;
import br.com.alura.AluraFake.user.model.Role;
import br.com.alura.AluraFake.user.model.User;
import br.com.alura.AluraFake.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseValidator courseValidator;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private List<Task> tasks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        User instructor = mock(User.class);
        when(instructor.isInstructor()).thenReturn(true);

        course = new Course("Java", "Curso de Java", instructor);

        Task t1 = new Task();
        t1.setType(TaskType.OPEN_TEXT);
        t1.setTaskOrder(1);

        Task t2 = new Task();
        t2.setType(TaskType.SINGLE_CHOICE);
        t2.setTaskOrder(2);

        Task t3 = new Task();
        t3.setType(TaskType.MULTIPLE_CHOICE);
        t3.setTaskOrder(3);

        tasks = Arrays.asList(t1, t2, t3);
    }

    @Test
    void publishCourse_should_publish_course_successfully() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseIdOrderByTaskOrder(1L)).thenReturn(tasks);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Course published = courseService.publishCourse(1L);

        verify(courseValidator, times(1)).validateBeforePublish(course, tasks);
        assertEquals(Status.PUBLISHED, published.getStatus());
        assertNotNull(published.getPublishedAt());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void publishCourse_should_throw_when_course_not_found() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> courseService.publishCourse(1L));

        assertEquals("Curso não encontrado", ex.getMessage());
    }

    @Test
    void publishCourse_should_throw_when_validator_fails() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseIdOrderByTaskOrder(1L)).thenReturn(tasks);

        doThrow(new IllegalStateException("Curso não pode ser publicado"))
                .when(courseValidator).validateBeforePublish(course, tasks);

        Exception ex = assertThrows(IllegalStateException.class,
                () -> courseService.publishCourse(1L));

        assertEquals("Curso não pode ser publicado", ex.getMessage());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void getCoursesByInstructor_should_return_report_when_instructor_exists() {
        Long instructorId = 1L;

        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        when(userRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        InstructorCourseReportDTO course1 = new InstructorCourseReportDTO(
                1L, "Java", Status.BUILDING, LocalDateTime.of(2025, 10, 6, 10, 0), 5L
        );
        InstructorCourseReportDTO course2 = new InstructorCourseReportDTO(
                2L, "Spring", Status.PUBLISHED, LocalDateTime.of(2025, 10, 6, 10, 0), 3L
        );

        List<InstructorCourseReportDTO> courseList = Arrays.asList(course1, course2);
        when(courseRepository.findCoursesByInstructorId(instructorId)).thenReturn(courseList);

        InstructorCourseReportResponseDTO report = courseService.getCoursesByInstructor(instructorId);

        assertNotNull(report);
        assertEquals(2, report.totalCourses());
        assertEquals(courseList, report.courses());

        verify(userRepository, times(1)).findById(instructorId);
        verify(courseRepository, times(1)).findCoursesByInstructorId(instructorId);
    }

    @Test
    void getCoursesByInstructor_should_throw_exception_when_user_not_found() {
        Long instructorId = 2L;

        when(userRepository.findById(instructorId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseService.getCoursesByInstructor(instructorId)
        );

        assertEquals("Instrutor não encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById(instructorId);
        verifyNoInteractions(courseRepository);
    }

    @Test
    void getCoursesByInstructor_should_throw_exception_when_user_is_not_instructor() {
        Long instructorId = 3L;

        User user = new User("Maria", "maria@alura.com.br", Role.STUDENT);
        when(userRepository.findById(instructorId)).thenReturn(Optional.of(user));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> courseService.getCoursesByInstructor(instructorId)
        );

        assertEquals("Usuário não é um instrutor", exception.getMessage());
        verify(userRepository, times(1)).findById(instructorId);
        verifyNoInteractions(courseRepository);
    }
}
