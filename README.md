# Sistema de Ponto Eletrônico

## Diagrama
<img width="1019" height="535" alt="image" src="https://github.com/user-attachments/assets/f34d19e9-1208-4030-bfd3-839902c99fcc" />

## Resumo

Este projeto apresenta o desenvolvimento de um **Sistema de Ponto Eletrônico** utilizando a linguagem Java, com foco na aplicação de **boas práticas de modelagem orientada a objetos**, **persistência com JPA/Hibernate** e **organização em camadas**. O sistema permite o registro de jornada de trabalho, identificação automática de inconsistências e suporte à gestão por meio de hierarquia entre gestores e funcionários.

---

## Objetivo

O objetivo do projeto é implementar um sistema acadêmico que simule, de forma fiel, um controle de ponto corporativo, explorando conceitos como:

- Herança e polimorfismo
- Relacionamentos entre entidades
- Persistência em banco de dados relacional
- Integridade referencial
- Arquitetura em camadas

---

## Tecnologias Utilizadas

- Java (JDK 21 ou superior)
- JPA (Jakarta Persistence)
- Hibernate ORM
- Banco de Dados H2 (modo arquivo)
- Maven
- Execução em modo Console

---

## Arquitetura do Sistema

O sistema foi dividido em camadas bem definidas:

- **Camada de Apresentação (Console)**: interação com o usuário via terminal
- **Camada de Serviço**: regras de negócio e validações
- **Camada de Persistência**: acesso ao banco utilizando JPA
- **Camada de Domínio**: entidades e relacionamento entre classes

Essa separação melhora a manutenibilidade, legibilidade e extensibilidade do sistema.

---

## Modelagem do Domínio

A modelagem utiliza **herança do tipo JOINED**, representando diferentes tipos de funcionários:

- `Funcionario` (classe abstrata)
- `Gestor`
- `FuncionarioComum`

A hierarquia permite o uso de polimorfismo, eliminando flags booleanas e fortalecendo a coerência do domínio.

---

## Estrutura do Banco de Dados

O banco de dados é composto pelas seguintes tabelas principais:

- `FUNCIONARIO`: armazena dados comuns e o discriminador de tipo
- `GESTOR`: especialização de funcionário com papel gerencial
- `FUNCIONARIO_COMUM`: funcionário subordinado a um gestor
- `REGISTRO_PONTO`: registros de entrada e saída
- `INCONSISTENCIA`: irregularidades detectadas automaticamente

A relação hierárquica (gestor-funcionário) é aplicada exclusivamente aos funcionários comuns.

---

## Inicialização do Banco

O sistema utiliza um mecanismo de inicialização automática:

- `DatabaseInitializer`: cria o schema apenas se o banco não existir
- `DataLoader`: limpa o banco e insere dados de teste de forma controlada

Durante o desenvolvimento, o schema é recriado utilizando a propriedade:

```properties
hibernate.hbm2ddl.auto=create
```

---

## Funcionalidades Implementadas

### Funcionário Comum

- Registro de ponto
- Consulta de inconsistências

### Gestor

- Visualização da equipe
- Consulta de inconsistências da equipe
- Acesso a relatórios gerenciais

---

## Boas Práticas Aplicadas

- Uso adequado de herança e associação
- Eliminação de atributos redundantes
- Garantia de integridade referencial
- Persistência orientada ao domínio
- Código organizado e de fácil manutenção

---

## Considerações Finais

O projeto atende aos objetivos acadêmicos propostos, demonstrando domínio de conceitos fundamentais da engenharia de software, especialmente no que diz respeito à modelagem orientada a objetos e persistência com JPA.

O sistema está preparado para futuras evoluções, como migração para interface web ou adoção de bancos de dados corporativos.

---

## Autores
- Jaime Ronaldo dos Reis Santiago
- Adriano Nascimento Barbosa
- Jean Mateus Vasconcelos de Oliveira
- Hudson Oliveira Sena
- Gustavo Henrique Leles Coelho

Projeto desenvolvido para fins acadêmicos, com foco em aprendizado e aplicação prática de conceitos de sistemas orientados a objetos e persistência de dados.
