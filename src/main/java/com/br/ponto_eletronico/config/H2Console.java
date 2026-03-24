package com.br.ponto_eletronico.config;

import org.h2.tools.Server;

public class H2Console {

    public static void start() {
        try {
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("H2 Console iniciado em http://localhost:8082");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}