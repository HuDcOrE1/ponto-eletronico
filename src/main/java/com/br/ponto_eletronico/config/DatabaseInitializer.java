package com.br.ponto_eletronico.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:h2:file:./database/ponto";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void inicializar() {

        try {

            boolean bancoExiste = Files.exists(Paths.get("database/ponto.mv.db"));

            if (bancoExiste) {
                return;
            }

            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            String schema = Files.readString(
                    Paths.get("src/main/resources/database/schema.sql"));

            String data = Files.readString(
                    Paths.get("src/main/resources/database/data.sql"));

            stmt.execute(schema);
            stmt.execute(data);

            stmt.close();
            conn.close();

            System.out.println("Banco inicializado com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}