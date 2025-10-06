package br.com.alura.AluraFake.task.controller;

import br.com.alura.AluraFake.task.dto.NewTaskDTO;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.model.TaskOption;
import br.com.alura.AluraFake.task.model.TaskType;
import br.com.alura.AluraFake.task.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task/new")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    private Task buildTaskFromDto(NewTaskDTO dto, TaskType type) {
        Task task = new Task();
        task.setTaskOrder(dto.getOrder());
        task.setStatement(dto.getStatement());
        task.setType(type);

        if (dto.getOptions() != null && task.getType() != TaskType.OPEN_TEXT) {
            List<TaskOption> options = dto.getOptions().stream()
                    .map(o -> new TaskOption(task, o.getOption(), o.isCorrect()))
                    .toList();
            task.setOptions(options);
        }

        return task;
    }

    @PostMapping("/opentext")
    public ResponseEntity<Task> newOpenText(@RequestBody NewTaskDTO dto) {
        Task task = buildTaskFromDto(dto, TaskType.OPEN_TEXT);
        Task saved = taskService.createTask(dto.getCourseId(), task);

        return ResponseEntity.ok(saved);
    }

    @PostMapping("/singlechoice")
    public ResponseEntity<Task> newSingleChoice(@RequestBody NewTaskDTO dto) {
        Task task = buildTaskFromDto(dto, TaskType.SINGLE_CHOICE);
        Task saved = taskService.createTask(dto.getCourseId(), task);

        return ResponseEntity.ok(saved);
    }

    @PostMapping("/multiplechoice")
    public ResponseEntity<Task> newMultipleChoice(@RequestBody NewTaskDTO dto) {
        Task task = buildTaskFromDto(dto, TaskType.MULTIPLE_CHOICE);
        Task saved = taskService.createTask(dto.getCourseId(), task);

        return ResponseEntity.ok(saved);
    }
}
