# ponto-eletronico
Projeto base para o curso da ADATech

# Estrutura do projeto
ponto-eletronico
│
├── pom.xml
│
└── src/main/java/br/com/ponto
    │
    ├── app
    │     └── Application.java
    │
    ├── config
    │     ├── DataBaseInitialize.java
    │     ├── JPAUtil.java
    │     └── SqlFileLogger.java
    │
    ├── entity
    │     ├── Funcionario.java
    │     ├── Gestor.java
    │     └── RegistroPonto.java
    │     └── TipoRegistro.java
    │
    ├── repository
    │     ├── FuncionarioRepository.java
    │     └── RegistroPontoRepository.java
    │
    ├── service
    │     ├── AutenticacaoService.java
    │     └── PontoService.java
    │
    ├── exception
    │     ├── RegraPontoException.java
    │     └── AutenticacaoException.java
    │
    └── console
          └── MenuConsole.java
          
    O projeto está rodando com o Java 25 e apache-maven-3.9.14
    Está usando o banco H2, mas está fazendo um registro vitual no ponto.mv.db (ainda vou corrigir)
          
