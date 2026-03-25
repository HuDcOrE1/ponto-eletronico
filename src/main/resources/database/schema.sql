CREATE TABLE FUNCIONARIO (

                             ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                             MATRICULA VARCHAR(20) NOT NULL UNIQUE,
                             SENHA VARCHAR(255) NOT NULL,
                             NOME VARCHAR(100) NOT NULL,
                             TIPO_FUNCIONARIO VARCHAR(31) NOT NULL

);

CREATE TABLE GESTOR (

                        ID BIGINT PRIMARY KEY,

                        CONSTRAINT FK_GESTOR_FUNCIONARIO
                            FOREIGN KEY (ID)
                                REFERENCES FUNCIONARIO(ID)

);

CREATE TABLE FUNCIONARIO_COMUM (

                                   ID BIGINT PRIMARY KEY,
                                   GESTOR_ID BIGINT NOT NULL,

                                   CONSTRAINT FK_FUNC_COMUM_FUNCIONARIO
                                       FOREIGN KEY (ID)
                                           REFERENCES FUNCIONARIO(ID),

                                   CONSTRAINT FK_FUNC_COMUM_GESTOR
                                       FOREIGN KEY (GESTOR_ID)
                                           REFERENCES GESTOR(ID)

);

CREATE TABLE REGISTRO_PONTO (

                                ID BIGINT AUTO_INCREMENT PRIMARY KEY,

                                FUNCIONARIO_ID BIGINT NOT NULL,
                                HORARIO TIMESTAMP NOT NULL,
                                TIPO VARCHAR(30) NOT NULL,

                                CONSTRAINT FK_REGISTRO_FUNCIONARIO
                                    FOREIGN KEY (FUNCIONARIO_ID)
                                        REFERENCES FUNCIONARIO(ID)

);

CREATE TABLE IF NOT EXISTS INCONSISTENCIA (

                                              ID INTEGER PRIMARY KEY AUTOINCREMENT,
                                              FUNCIONARIO_ID INTEGER,
                                              DESCRICAO TEXT,
                                              HORARIO TEXT,

                                              FOREIGN KEY(FUNCIONARIO_ID)
    REFERENCES FUNCIONARIO(ID)

    );