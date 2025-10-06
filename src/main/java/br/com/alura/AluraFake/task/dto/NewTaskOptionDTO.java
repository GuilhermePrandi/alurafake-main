package br.com.alura.AluraFake.task.dto;

public class NewTaskOptionDTO {

    private String option;
    private boolean correct;

    public NewTaskOptionDTO() {}

    public NewTaskOptionDTO(String option, boolean correct) {
        this.option = option;
        this.correct = correct;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean isCorrect) {
        this.correct = isCorrect;
    }
}
