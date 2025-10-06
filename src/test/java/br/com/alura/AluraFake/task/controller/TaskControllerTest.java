package br.com.alura.AluraFake.task.controller;

import br.com.alura.AluraFake.task.dto.NewTaskDTO;
import br.com.alura.AluraFake.task.dto.NewTaskOptionDTO;
import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.model.TaskType;
import br.com.alura.AluraFake.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private NewTaskDTO dto;

    @BeforeEach
    void setUp() {
        dto = new NewTaskDTO();
        dto.setCourseId(1L);
        dto.setStatement("Default statement");
        dto.setOrder(1);
    }

    @Test
    void newOpenTextTask__should_return_ok_when_request_is_valid() throws Exception {
        Task savedTask = new Task();
        savedTask.setId(3L);
        savedTask.setStatement(dto.getStatement());
        savedTask.setTaskOrder(dto.getOrder());
        savedTask.setType(TaskType.OPEN_TEXT);

        when(taskService.createTask(eq(dto.getCourseId()), any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(taskService, times(1)).createTask(eq(dto.getCourseId()), any(Task.class));
    }

    @Test
    void newSingleChoiceTask__should_return_ok_when_request_is_valid() throws Exception {
        NewTaskOptionDTO opt1 = new NewTaskOptionDTO();
        opt1.setOption("Java");
        opt1.setCorrect(true);

        NewTaskOptionDTO opt2 = new NewTaskOptionDTO();
        opt2.setOption("Python");
        opt2.setCorrect(false);

        dto.setOptions(List.of(opt1, opt2));

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setStatement(dto.getStatement());
        savedTask.setTaskOrder(dto.getOrder());
        savedTask.setType(TaskType.SINGLE_CHOICE);

        when(taskService.createTask(eq(dto.getCourseId()), any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(taskService, times(1)).createTask(eq(dto.getCourseId()), any(Task.class));
    }

    @Test
    void newMultipleChoiceTask__should_return_ok_when_request_is_valid() throws Exception {
        NewTaskOptionDTO opt1 = new NewTaskOptionDTO();
        opt1.setOption("Java");
        opt1.setCorrect(true);

        NewTaskOptionDTO opt2 = new NewTaskOptionDTO();
        opt2.setOption("Spring");
        opt2.setCorrect(true);

        NewTaskOptionDTO opt3 = new NewTaskOptionDTO();
        opt3.setOption("Ruby");
        opt3.setCorrect(false);

        dto.setOptions(List.of(opt1, opt2, opt3));

        Task savedTask = new Task();
        savedTask.setId(2L);
        savedTask.setStatement(dto.getStatement());
        savedTask.setTaskOrder(dto.getOrder());
        savedTask.setType(TaskType.MULTIPLE_CHOICE);

        when(taskService.createTask(eq(dto.getCourseId()), any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(taskService, times(1)).createTask(eq(dto.getCourseId()), any(Task.class));
    }
}
