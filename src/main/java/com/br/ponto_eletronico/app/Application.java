package com.br.ponto_eletronico.app;

import com.br.ponto_eletronico.config.DataLoader;
import com.br.ponto_eletronico.config.DatabaseInitializer;
import com.br.ponto_eletronico.config.H2Console;
import com.br.ponto_eletronico.console.MenuConsole;

public class Application {

    public static void main(String[] args) {
        DatabaseInitializer.inicializar();
        DataLoader.load();
        H2Console.start();

        MenuConsole menu = new MenuConsole();
        menu.iniciar();

    }
}