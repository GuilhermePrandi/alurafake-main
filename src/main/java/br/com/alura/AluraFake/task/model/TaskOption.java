package br.com.alura.AluraFake.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "TaskOption")
public class TaskOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private Task task;

    @Column(name = "option_text", nullable = false, length = 80)
    private String option;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    public TaskOption() {}

    public TaskOption(Task task, String option, boolean isCorrect) {
        this.task = task;
        this.option = option;
        this.isCorrect = isCorrect;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public String getOption() { return option; }
    public void setOption(String option) { this.option = option; }

    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
}
