package com.br.ponto_eletronico.console;

import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.service.FuncionarioService;
import com.br.ponto_eletronico.service.InconsistenciaService;
import com.br.ponto_eletronico.service.RelatorioService;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class GerenciaConsole {
    private Scanner scanner;
    private FuncionarioService funcionarioService;
    private InconsistenciaService inconsistenciaService;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private RelatorioService relatorioService = new RelatorioService();
    private RelatorioConsole relatorioConsole;


    public GerenciaConsole(Scanner scanner, FuncionarioService funcionarioService, InconsistenciaService inconsistenciaService) {
        this.scanner = scanner;
        this.funcionarioService = funcionarioService;
        this.inconsistenciaService = inconsistenciaService;
        this.relatorioConsole = new RelatorioConsole(scanner, funcionarioService, relatorioService);
    }

    public void menu() {
        System.out.println("\n=== GERÊNCIA DE PONTO ===");
        System.out.println("1 - Buscar funcionário por matrícula");
        System.out.println("2 - Alterar ponto do funcionário");
        System.out.println("3 - Relatórios Gerenciais");
        System.out.println("0 - Voltar");

        int opGerencia = scanner.nextInt();
        scanner.nextLine();

        switch (opGerencia){
            case 1:
                buscarFuncionarioPorMatricula();
                break;
            case 3:
                relatorioConsole.exibirRelatorios(scanner);
                break;
            default:
                break;
        }
    }

    private void buscarFuncionarioPorMatricula() {
        System.out.print("Digite a matrícula: ");
        String matricula = scanner.nextLine();

        Funcionario f = funcionarioService.buscarPorMatricula(matricula);

        if (f == null) {
            System.out.println("Funcionário não encontrado.");

            return;
        }

        System.out.println("\nFuncionário localizado:");
        System.out.println("Nome: " + f.getNome());
        System.out.println("Matrícula: " + f.getMatricula());

        var inconsistencias = inconsistenciaService.listarPorFuncionario(f);

        if (inconsistencias.isEmpty()) {
            System.out.println("Sem inconsistências registradas.");

            return;
        }

        System.out.println("Existem inconsistências registradas.");

        YearMonth periodo = lerPeriodo();

        var inconsistenciasPeriodo = inconsistenciaService.listarPorPeriodo(inconsistencias, periodo);

        if (inconsistenciasPeriodo.isEmpty()) {
            System.out.println("Sem inconsistências para o período informado.");

            return;
        }

        inconsistenciasPeriodo.forEach(i -> {
            String horaFmt = i.getHorario().format(format);

            System.out.println(horaFmt + " - " + i.getDescricao());
        });
    }

    private YearMonth lerPeriodo() {
        DateTimeFormatter formatoMesAno = DateTimeFormatter.ofPattern("MM/yyyy");
        YearMonth periodo = null;

        while (periodo == null) {
            System.out.print("Informe a competência para visualização (MM/yyyy): ");
            String periodoStr = scanner.nextLine();

            try {
                periodo = YearMonth.parse(periodoStr, formatoMesAno);
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use MM/yyyy\n");
            }
        }

        return periodo;
    }
}
