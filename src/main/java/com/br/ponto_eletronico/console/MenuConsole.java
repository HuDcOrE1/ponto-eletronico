package com.br.ponto_eletronico.console;



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
            System.out.println("2 - Consultar inconsistências");
            if (funcionario.isGestor())
                System.out.println("3 - Gerência de ponto");
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
                    lista.forEach(i ->
                        System.out.println(
                            i.getHorario() + " - " + i.getDescricao()
                        )
                    );
                }

            }

            if (op == 3 && funcionario.isGestor()) {
                System.out.println("\n=== GERÊNCIA DE PONTO ===");
                System.out.println("1 - Buscar funcionário por matrícula");
                System.out.println("2 - Alterar ponto do funcionário");
                System.out.println("0 - Voltar");

                int opGerencia = scanner.nextInt();
                scanner.nextLine();

                if (opGerencia == 1) {
                    System.out.print("Digite a matrícula: ");
                    String matricula = scanner.nextLine();

                    Funcionario f = funcionarioService.buscarPorMatricula(matricula);

                    if (f != null) {
                        System.out.println("\nFuncionário localizado:");
                        System.out.println("Nome: " + f.getNome());
                        System.out.println("Matrícula: " + f.getMatricula());
                    } else {
                        System.out.println("Funcionário não encontrado.");
                    }
                }
            }

            if (op == 0) {
                break;
            }
        }
    }
}