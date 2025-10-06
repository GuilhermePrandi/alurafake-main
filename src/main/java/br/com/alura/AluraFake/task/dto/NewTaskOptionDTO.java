package br.com.alura.AluraFake.task.dto;

public class NewTaskOptionDTO {

    private String option;
    private boolean isCorrect;

    public NewTaskOptionDTO() {}

    public NewTaskOptionDTO(String option, boolean isCorrect) {
        this.option = option;
        this.isCorrect = isCorrect;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
