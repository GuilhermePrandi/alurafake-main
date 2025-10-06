package br.com.alura.AluraFake.task.validator;

import br.com.alura.AluraFake.task.model.Task;
import br.com.alura.AluraFake.task.model.TaskOption;
import br.com.alura.AluraFake.task.model.TaskType;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TaskValidator {

    public void validateTask(Task task) {
        validateStatement(task.getStatement());
        validateOrder(task.getTaskOrder());
        validateOptions(task);
    }

    private void validateStatement(String statement) {
        if (statement == null || statement.length() < 4 || statement.length() > 255) {
            throw new IllegalArgumentException("O enunciado deve ter entre 4 e 255 caracteres");
        }
    }

    private void validateOrder(int order) {
        if (order < 1) throw new IllegalArgumentException("Ordem deve ser positiva");
    }

    private void validateOptions(Task task) {
        if (task.getType() == TaskType.OPEN_TEXT) return;

        if (task.getOptions() == null) {
            throw new IllegalArgumentException("Opções não podem ser nulas");
        }

        validateOptionCountAndCorrectness(task);
        validateOptionTexts(task);
    }

    private void validateOptionCountAndCorrectness(Task task) {
        long correctCount = task.getOptions().stream().filter(TaskOption::isCorrect).count();
        int total = task.getOptions().size();

        switch (task.getType()) {
            case SINGLE_CHOICE -> {
                if (total < 2 || total > 5)
                    throw new IllegalArgumentException("Deve ter entre 2 e 5 alternativas");
                if (correctCount != 1)
                    throw new IllegalArgumentException("Deve ter exatamente uma alternativa correta");
            }
            case MULTIPLE_CHOICE -> {
                if (total < 3 || total > 5)
                    throw new IllegalArgumentException("Deve ter entre 3 e 5 alternativas");
                if (correctCount < 2)
                    throw new IllegalArgumentException("Deve ter duas ou mais alternativas corretas");
                if (correctCount == total)
                    throw new IllegalArgumentException("Deve ter pelo menos uma alternativa incorreta");
            }
        }
    }

    private void validateOptionTexts(Task task) {
        Set<String> optionTexts = new HashSet<>();
        for (TaskOption option : task.getOptions()) {
            String text = option.getOption();
            if (text == null || text.length() < 4 || text.length() > 80)
                throw new IllegalArgumentException("Cada opção deve ter entre 4 e 80 caracteres");
            if (text.equals(task.getStatement()))
                throw new IllegalArgumentException("Opção não pode ser igual ao enunciado");
            if (!optionTexts.add(text))
                throw new IllegalArgumentException("Opções repetidas não são permitidas");
        }
    }
}
