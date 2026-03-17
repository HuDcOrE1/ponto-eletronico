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
          
*** ==================== *** - MVP - *** ==================== ***
Personas:
      - Gestor
      - Funcionário
      
Fluxo:
Login por Matrícula e senha
      Funcionário
      Tem acesso - Menu:
                              1) Bater Ponto (regras intervalo entre as 2 e a 4 hora - jornada de 6 horas)
                              2) Consultar Ponto (Jean)
                              3) Sair
                              
      Não tem acesso - Mensagem: "Usuário não encontrado"
                              
      Gestor
      Tem acesso - Menu
                              1) Bater
                              2) Consultar Ponto
                              3) Gerir ponto
                                    3.1) Consultar Funcionário (Jaime - Consultar pela matrícula)
                                    3.2) Alterar ponto Funcionário (Adriano)
                                    3.2) Emitir relatório (CSV) (Gustavo)
                              4) Sair