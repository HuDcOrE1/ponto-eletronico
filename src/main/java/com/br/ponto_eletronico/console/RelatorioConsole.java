package com.br.ponto_eletronico.console;

import com.br.ponto_eletronico.Records.Relatorio.ControleDiario;
import com.br.ponto_eletronico.Records.Relatorio.GestaoHoras;
import com.br.ponto_eletronico.Records.Relatorio.SituacaoMensal;
import com.br.ponto_eletronico.Records.Util.Competencia;
import com.br.ponto_eletronico.entity.FuncionarioComum;
import com.br.ponto_eletronico.entity.Gestor;
import com.br.ponto_eletronico.service.FuncionarioService;
import com.br.ponto_eletronico.service.RelatorioService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class RelatorioConsole {

    private Scanner scanner;
    private RelatorioService relatorioService;
    private FuncionarioService funcionarioService;
    private Gestor gestor;

    public RelatorioConsole (Scanner scanner, FuncionarioService funcionarioService, RelatorioService relatorioService, Gestor gestor){
        this.scanner = scanner;
        this.funcionarioService = funcionarioService;
        this.relatorioService = relatorioService;
        this.gestor = gestor;
    }

    public void exibirRelatorios(Scanner scanner) {

        System.out.println("\n=== RELATÓRIOS GERENCIAIS ===");
        System.out.println("1 - Controle diário");
        System.out.println("2 - Gestão de horas");
        System.out.println("3 - Situação do mês");
        System.out.println("0 - Voltar");
        Integer opRelatorio = scanner.nextInt();

        switch (opRelatorio) {
            case 0:
                break;
            case 1:
                System.out.println("Relatório: Controle diário");
                espelhoPonto(scanner, opRelatorio);
                break;

            case 2:
                System.out.println("Relatório: Gestão de horas");
                bancoHoras(scanner, opRelatorio);
                break;

            case 3:
                System.out.println("Relatório: Situação do mês");
                situacaoMensal(scanner, opRelatorio);
                break;

            case 4:
                System.out.println("Saindo...");
                break;

            default:
                System.out.println("Opção inválida!");
        }
    }

    public void espelhoPonto(Scanner scanner, int op) {

        FuncionarioComum funcionario = solicitarFuncionarioValido(scanner);

        if (funcionario == null) {
            System.out.println("Operação cancelada.\n");
            return;
        }

        System.out.println("\nFuncionário localizado:");
        System.out.println("Nome: " + funcionario.getNome());
        System.out.println("Matrícula: " + funcionario.getMatricula());

        ControleDiario relatorioCD =
                relatorioService.gerarRelatorioControleDiario(funcionario, gestor);

        System.out.println(relatorioCD);

        Path basePath = Paths.get("relatorio", "ControleDiario");

        solicitarExportacaoCsv(
                scanner,
                op,
                basePath,
                0,
                0,
                funcionario,
                gestor
        );
    }

    public void bancoHoras(Scanner scanner, int op) {

        FuncionarioComum funcionario = solicitarFuncionarioValido(scanner);

        if (funcionario == null) {
            System.out.println("Operação cancelada.\n");
            return;
        }

        System.out.println("\nFuncionário localizado:");
        System.out.println("Nome: " + funcionario.getNome());
        System.out.println("Matrícula: " + funcionario.getMatricula());

        Competencia competencia = solicitarCompetencia(scanner);

        List<GestaoHoras> list =
                relatorioService.gerarRelatorioGestaoHoras(
                        funcionario,
                        competencia.ano(),
                        competencia.mes(),
                        gestor
                );

        list.forEach(System.out::println);

        Path basePath = Paths.get("relatorio", "GestaoHoras");

        solicitarExportacaoCsv(
                scanner,
                op,
                basePath,
                competencia.ano(),
                competencia.mes(),
                funcionario,
                gestor
        );
    }

    private FuncionarioComum solicitarFuncionarioValido(Scanner scanner) {
        scanner.nextLine();
        while (true) {

            System.out.print("Digite a matrícula (0 para cancelar): ");
            String matricula = scanner.nextLine();

            if ("0".equals(matricula)) {
                return null; // ✔ cancelado
            }

            FuncionarioComum funcionario =
                    funcionarioService.buscarPorMatricula(matricula);

            if (Objects.isNull(funcionario)) {
                System.out.println("Funcionário não encontrado.\n");
                continue;
            }

            if (!verificaGestor(funcionario, gestor)) {
                System.out.println("Funcionário não pertence a este gestor.\n");
                continue;
            }

            return funcionario; // ✔ válido
        }
    }

    public boolean verificaGestor(FuncionarioComum funcionarioComum, Gestor gestor) {
        return funcionarioComum != null
                && gestor != null
                && funcionarioComum.getGestor() != null
                && funcionarioComum.getGestor().getId().equals(gestor.getId());
    }

    public void situacaoMensal(Scanner scanner, int op){
        scanner.nextLine();
        Competencia competencia = solicitarCompetencia(scanner);
        List<SituacaoMensal> list = relatorioService.gerarRelatorioMensal(competencia.ano(), competencia.mes(), gestor);
        list.forEach(System.out::println);

        Path basePath = Paths.get("relatorio", "SituacaoMensal");
        solicitarExportacaoCsv(scanner, op, basePath,competencia.ano(), competencia.mes(), null, gestor);
    }

    public void solicitarExportacaoCsv(Scanner scanner, int op, Path ABSOLUTE_PATH, int ano, int mes, FuncionarioComum funcionario, Gestor gestor){
        System.out.println("\n=== EXPORTAR RELATÓRIO PARA CSV ===");
        System.out.println("0 - NÃO");
        System.out.println("1 - SIM");
        Integer opCsv = scanner.nextInt();

        if (opCsv == 1){
            String output = relatorioService.gerarCsv(op, ano, mes, ABSOLUTE_PATH, funcionario, gestor);
            System.out.println(output);
        }
    }

    public Competencia solicitarCompetencia(Scanner scanner) {

        int mes;
        int ano;

        while (true) {
            System.out.print("Digite a competência (MM/AAAA): ");
            String competencia = scanner.nextLine();

            try {
                String[] partes = competencia.split("/");

                if (partes.length != 2) {
                    throw new IllegalArgumentException();
                }

                mes = Integer.parseInt(partes[0]);
                ano = Integer.parseInt(partes[1]);

                if (mes < 1 || mes > 12) {
                    throw new IllegalArgumentException();
                }

                return new Competencia(mes, ano);

            } catch (Exception e) {
                System.out.println("Competência inválida! Use MM/AAAA (ex: 03/2026)");
            }
        }
    }
}
