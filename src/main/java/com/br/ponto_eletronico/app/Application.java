package com.br.ponto_eletronico.app;

import com.br.ponto_eletronico.config.DatabaseInitializer;
import com.br.ponto_eletronico.console.MenuConsole;

public class Application {

    public static void main(String[] args) {

        DatabaseInitializer.inicializar();

        MenuConsole menu = new MenuConsole();
        menu.iniciar();

    }
}