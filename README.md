# AluraFake 📘

AluraFake é um projeto simulado inspirado na plataforma Alura, desenvolvido como code challenge para fins de avaliação técnica. Ele implementa funcionalidades básicas de gerenciamento de usuários, cursos e tarefas, incluindo regras de acesso baseadas em perfis (roles) e integração com banco de dados.

## Tecnologias 💻

Java 21

Spring Boot 3.3

JPA/Hibernate

MySQL (ou H2 para testes)

Flyway para migrações de banco de dados

JUnit 5, MockMvc para testes

GitHub Actions para CI

## Funcionalidades principais 📸

- Cadastro, listagem e consulta de usuários, cursos e tarefas.
- Relatórios de cursos por instrutor.
- Criação de tarefas, cursos e geração de relatórios restritos a instrutores.
- Endpoints de listagem acessíveis a qualquer usuário autenticado.
- Migrações automáticas de banco via Flyway.

## Estrutura das Tasks (Atividades) e Course (Cursos) ⚙️

- Controller: define os endpoints e encaminha requisições para o serviço.
- Service: contém a lógica de negócio e regras de validação antes de persistir os dados.
- Validator: valida regras específicas de negócio, como campos obrigatórios e consistência de dados.
- Model: representa as entidades do sistema mapeadas para o banco via JPA/Hibernate.
- DTOs: objetos de transferência de dados entre Service e Controller, sem expor diretamente as entidades.

## Estrutura do Banco de Dados ⚙️

O banco de dados do projeto contém quatro tabelas principais:

- **user**: armazena os usuários do sistema, incluindo alunos e instrutores.
- **course**: armazena os cursos criados pelos instrutores.
- **task**: armazena as tarefas/atividades associadas a cada curso.
- **task_option**: armazena as opções de respostas das tarefas do tipo escolha (single ou multiple choice).


## Como rodar o projeto ⏯️

mvn spring-boot:run

## Criação de atividades 📇

- Atividade de Resposta Aberta
  
```bash
{
  "courseId": 1,
  "statement": "Descreva o que é JVM.",
  "order": 1
}
```

— Atividade de alternativa única 

```bash
{
  "courseId": 1,
  "statement": "Qual alternativa contém a sintaxe correta para declarar uma variável em Java?",
  "order": 2,
  "options": [
    {"option": "int numero = 10;", "correct": true},
    {"option": "numero int = 10;", "correct": false},
    {"option": "int = numero 10;", "correct": false},
    {"option": "10 = int numero;", "correct": false}
  ]
 }
```

-  Atividade de múltipla escolha

```bash
{
  "courseId": 1,
  "statement": "Quais são tipos primitivos do Java?",
  "order": 3,
  "options": [
    {"option": "int", "correct": true},
    {"option": "String", "correct": false},
    {"option": "boolean", "correct": true},
    {"option": "List", "correct": false}
  ]
}
```

## Publicação de Cursos 👓

```bash
curl -X POST "http://localhost:8080/course/1/publish"
```

## Relatório de cursos por instrutor

```bash
curl -X GET "http://localhost:8080/course/instructor/1/courses"
```
