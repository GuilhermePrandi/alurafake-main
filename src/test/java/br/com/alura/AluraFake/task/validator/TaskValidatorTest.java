package br.com.alura.AluraFake.task.validator;

import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.model.TaskOption;
import br.com.alura.AluraFake.task.model.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskValidatorTest {

    private TaskValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TaskValidator();
    }

    @Test
    void validateTask_should_throw_when_statement_is_invalid() {
        Task task = new Task();
        task.setTaskOrder(1);
        task.setType(TaskType.OPEN_TEXT);
        task.setStatement("abc");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validateTask(task));

        assertEquals("O enunciado deve ter entre 4 e 255 caracteres", ex.getMessage());
    }

    @Test
    void validateTask_should_throw_when_order_is_invalid() {
        Task task = new Task();
        task.setTaskOrder(0);
        task.setType(TaskType.OPEN_TEXT);
        task.setStatement("Valid statement");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validateTask(task));

        assertEquals("Ordem deve ser positiva", ex.getMessage());
    }

    @Test
    void validateTask_should_pass_for_valid_open_text_task() {
        Task task = new Task();
        task.setTaskOrder(1);
        task.setType(TaskType.OPEN_TEXT);
        task.setStatement("Valid statement");

        assertDoesNotThrow(() -> validator.validateTask(task));
    }

    @Test
    void validateTask_should_throw_when_single_choice_has_invalid_options() {
        Task task = new Task();
        task.setTaskOrder(1);
        task.setType(TaskType.SINGLE_CHOICE);
        task.setStatement("Choose one");

        TaskOption onlyOption = new TaskOption(task, "Option1", true);
        task.setOptions(List.of(onlyOption));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validateTask(task));
        assertEquals("Deve ter entre 2 e 5 alternativas", ex.getMessage());
    }

    @Test
    void validateTask_should_throw_when_single_choice_has_multiple_correct_options() {
        Task task = new Task();
        task.setTaskOrder(1);
        task.setType(TaskType.SINGLE_CHOICE);
        task.setStatement("Choose one");

        TaskOption o1 = new TaskOption(task, "Option1", true);
        TaskOption o2 = new TaskOption(task, "Option2", true);
        task.setOptions(List.of(o1, o2));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validateTask(task));
        assertEquals("Deve ter exatamente uma alternativa correta", ex.getMessage());
    }

    @Test
    void validateTask_should_pass_for_valid_single_choice() {
        Task task = new Task();
        task.setTaskOrder(1);
        task.setType(TaskType.SINGLE_CHOICE);
        task.setStatement("Pick one");

        TaskOption o1 = new TaskOption(task, "Option1", true);
        TaskOption o2 = new TaskOption(task, "Option2", false);
        task.setOptions(List.of(o1, o2));

        assertDoesNotThrow(() -> validator.validateTask(task));
    }

    @Test
    void validateTask_should_throw_when_multiple_choice_invalid_options() {
        Task task = new Task();
        task.setTaskOrder(1);
        task.setType(TaskType.MULTIPLE_CHOICE);
        task.setStatement("Pick several");

        TaskOption o1 = new TaskOption(task, "Option1", true);
        TaskOption o2 = new TaskOption(task, "Option2", false);
        task.setOptions(List.of(o1, o2, o2));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validateTask(task));

        assertTrue(ex.getMessage().contains("Deve ter duas ou mais alternativas corretas") ||
                ex.getMessage().contains("Opções repetidas não são permitidas"));
    }

    @Test
    void validateTask_should_pass_for_valid_multiple_choice() {
        Task task = new Task();
        task.setTaskOrder(1);
        task.setType(TaskType.MULTIPLE_CHOICE);
        task.setStatement("Pick several");

        TaskOption o1 = new TaskOption(task, "Option1", true);
        TaskOption o2 = new TaskOption(task, "Option2", true);
        TaskOption o3 = new TaskOption(task, "Option3", false);
        task.setOptions(List.of(o1, o2, o3));

        assertDoesNotThrow(() -> validator.validateTask(task));
    }

    @Test
    void validateTask_should_throw_for_option_text_too_short_or_equal_to_statement() {
        Task task = new Task();
        task.setTaskOrder(1);
        task.setType(TaskType.SINGLE_CHOICE);
        task.setStatement("Statement");

        TaskOption o1 = new TaskOption(task, "St", true);
        TaskOption o2 = new TaskOption(task, "Statement", false);
        task.setOptions(List.of(o1, o2));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validateTask(task));

        assertTrue(ex.getMessage().contains("Cada opção deve ter entre 4 e 80 caracteres") ||
                ex.getMessage().contains("Opção não pode ser igual ao enunciado"));
    }

    @Test
    void validateTask_should_throw_for_duplicate_options() {
        Task task = new Task();
        task.setTaskOrder(1);
        task.setType(TaskType.SINGLE_CHOICE);
        task.setStatement("Statement");

        TaskOption o1 = new TaskOption(task, "Option1", true);
        TaskOption o2 = new TaskOption(task, "Option1", false);
        task.setOptions(List.of(o1, o2));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validateTask(task));

        assertEquals("Opções repetidas não são permitidas", ex.getMessage());
    }
}
