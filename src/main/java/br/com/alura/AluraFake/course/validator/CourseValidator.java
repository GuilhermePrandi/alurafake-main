package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.model.Status;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.model.TaskType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CourseValidator {

    public void validateBeforePublish(Course course, List<Task> tasks) {
        validateCourseStatus(course);
        validateTaskTypes(tasks);
        validateTaskOrder(tasks);
    }

    private void validateCourseStatus(Course course) {
        if (course.getStatus() != Status.BUILDING) {
            throw new IllegalStateException("Somente cursos em BUILDING podem ser publicados");
        }
    }

    private void validateTaskTypes(List<Task> tasks) {
        Set<TaskType> types = tasks.stream()
                .map(Task::getType)
                .collect(Collectors.toSet());

        if (!types.containsAll(Set.of(TaskType.OPEN_TEXT, TaskType.SINGLE_CHOICE, TaskType.MULTIPLE_CHOICE))) {
            throw new IllegalStateException("Curso deve ter ao menos uma atividade de cada tipo");
        }
    }

    private void validateTaskOrder(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskOrder() != i + 1) {
                throw new IllegalStateException(
                        "Atividade com id " + tasks.get(i).getId() +
                                " tem ordem incorreta: " + tasks.get(i).getTaskOrder()
                );
            }
        }
    }
}
