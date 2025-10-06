package br.com.alura.AluraFake.task.dto;

import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.model.TaskType;

public record TaskListItemDTO(
        Long id,
        String statement,
        TaskType type,
        int taskOrder

) {
    public TaskListItemDTO(Task task) {
        this(task.getId(), task.getStatement(), task.getType(), task.getTaskOrder());
    }
}
