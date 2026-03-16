package com.br.ponto_eletronico.console;



import java.util.Scanner;

import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.exception.AutenticacaoException;
import com.br.ponto_eletronico.exception.RegraPontoException;
import com.br.ponto_eletronico.service.AutenticacaoService;
import com.br.ponto_eletronico.service.PontoService;

public class MenuConsole {

    private Scanner scanner = new Scanner(System.in);

    private AutenticacaoService authService = new AutenticacaoService();
    private PontoService pontoService = new PontoService();

    public void iniciar() {

        System.out.println("=== SISTEMA DE PONTO ===");

        System.out.print("Matricula: ");
        String matricula = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        try {

            Funcionario funcionario =
                    authService.login(matricula, senha);

            menu(funcionario);

        } catch (AutenticacaoException e) {
            System.out.println(e.getMessage());
        }

    }

    private void menu(Funcionario funcionario) {

        while (true) {

            System.out.println("\n1 - Bater ponto");
            System.out.println("2 - Sair");

            int op = scanner.nextInt();

            if (op == 1) {

                try {
                    pontoService.baterPonto(funcionario);
                } catch (RegraPontoException e) {
                    System.out.println(e.getMessage());
                }

            }

            if (op == 2) {
                break;
            }

        }

    }

}