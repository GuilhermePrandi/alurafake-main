package br.com.alura.AluraFake;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @GetMapping
    public String home() {
        return """
                <h1>Code-Challenge Alura :)</h1>
                <ul>
                    <li><a href="/user/all">Usuários cadastrados</a></li>
                    <li><a href="/course/all">Cursos cadastrados</a></li>
                    <li><a href="/task/all">Tarefas cadastradas</a></li>
                    <li><a href="/course/1/publish">Publicar Curso 1 - Java</a></li>
                    <li><a href="/course/2/publish">Publicar Curso 2 - Python</a></li>
                    <li><a href="/course/instructor/1/courses">Relatório de cursos do instrutor Caio #1</a></li>
                    <li><a href="/course/instructor/2/courses">Relatório de cursos do instrutor Paulo #2</a></li>
                </ul>
                """;
    }
}
