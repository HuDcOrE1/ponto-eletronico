package com.br.ponto_eletronico.console;


import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.exception.AutenticacaoException;
import com.br.ponto_eletronico.exception.RegraPontoException;
import com.br.ponto_eletronico.service.AutenticacaoService;
import com.br.ponto_eletronico.service.FuncionarioService;
import com.br.ponto_eletronico.service.InconsistenciaService;
import com.br.ponto_eletronico.service.PontoService;

public class MenuConsole {

    private Scanner scanner = new Scanner(System.in);

    private AutenticacaoService authService = new AutenticacaoService();
    private PontoService pontoService = new PontoService();
    private InconsistenciaService inconsistenciaService =
            new InconsistenciaService();
    private FuncionarioService funcionarioService = new FuncionarioService();
    private GerenciaConsole gerenciaConsole = new GerenciaConsole(scanner, funcionarioService, inconsistenciaService);

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
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            System.out.println("\n1 - Bater ponto");
            System.out.println("2 - Consultar inconsistências");
            if (funcionario.isGestor())
                System.out.println("3 - Gerência de ponto");
            //Descomentar apenas para gerar massa de dados
            //System.out.println("4 - Gerar pontos");
            System.out.println("0 - Sair");

            int op = scanner.nextInt();

            if (op == 1) {

                try {
                    pontoService.baterPonto(funcionario);
                } catch (RegraPontoException e) {
                    System.out.println(e.getMessage());
                }

            }

            if (op == 2) {

                var lista =
                        inconsistenciaService.listarPorFuncionario(funcionario);

                if (lista.isEmpty()) {
                    System.out.println("Nenhuma inconsistência encontrada.");
                } else {

                    lista.forEach(i -> {


                        String horaFmt = i.getHorario().format(format);

                        System.out.println(
                                horaFmt + " - " + i.getDescricao()
                        );
                    });

                }

            }

            if (op == 3 && funcionario.isGestor()) {
                gerenciaConsole.menu();
            }

            //Descomentar apenas para gerar massa de dados
            /*if (op == 4) {

                try {
                    GeradorPontos.executar();
                } catch (RegraPontoException e) {
                    System.out.println(e.getMessage());
                }

            }*/

            if (op == 0) {
                break;
            }
        }
    }
}