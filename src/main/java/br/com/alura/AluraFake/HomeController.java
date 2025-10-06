package br.com.alura.AluraFake;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @GetMapping
    public String home() {
        return """
            <h1>Bem vindo ao teste de java Alura</h1>
            <ul>
                <li><a href="/user/all">Usuários cadastrados</a></li>
                <li><a href="/course/all">Cursos cadastrados</a></li>
                <li><a href="/course/instructor/2/courses">Relatório de cursos do instrutor #2</a></li>
            </ul>
            """;
    }
}
