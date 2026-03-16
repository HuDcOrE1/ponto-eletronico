package com.br.ponto_eletronico.config;

import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.entity.TipoRegistro;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class SqlFileLogger {

    private static final String FILE_PATH =
            "src/main/resources/database/db.sql";

    public static void registrarPonto(Funcionario funcionario,
                                      LocalDateTime horario,
                                      TipoRegistro tipo) {

        String sql = String.format(
                "INSERT INTO REGISTRO_PONTO (FUNCIONARIO_ID, HORARIO, TIPO) VALUES (%d, '%s', '%s');",
                funcionario.getId(),
                horario,
                tipo.name()
        );

        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {

            writer.write(sql);
            writer.write("\n");

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}