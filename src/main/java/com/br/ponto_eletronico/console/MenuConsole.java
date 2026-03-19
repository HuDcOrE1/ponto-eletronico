package com.br.ponto_eletronico.console;



import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.br.ponto_eletronico.DTO.Relatorio.ControleDiarioDTO;
import com.br.ponto_eletronico.DTO.Relatorio.GestaoHorasDTO;
import com.br.ponto_eletronico.DTO.Relatorio.SituacaoMensalDTO;
import com.br.ponto_eletronico.DTO.Util.CompetenciaDTO;
import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.exception.AutenticacaoException;
import com.br.ponto_eletronico.exception.RegraPontoException;
import com.br.ponto_eletronico.service.*;

public class MenuConsole {

    private Scanner scanner = new Scanner(System.in);

    private AutenticacaoService authService = new AutenticacaoService();
    private PontoService pontoService = new PontoService();
    private InconsistenciaService inconsistenciaService =
        new InconsistenciaService();
    private FuncionarioService funcionarioService = new FuncionarioService();
    private RelatorioService relatorioService = new RelatorioService();

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
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    lista.forEach(i ->{

                        
                        String horaFmt = i.getHorario().format(format);
                        
                        System.out.println(
                            horaFmt + " - " + i.getDescricao()
                        );
                    });
                
                }

            }

            if (op == 3 && funcionario.isGestor()) {
                System.out.println("\n=== GERÊNCIA DE PONTO ===");
                System.out.println("1 - Buscar funcionário por matrícula");
                System.out.println("2 - Alterar ponto do funcionário");
                System.out.println("3 - Relatórios gerenciais");
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
                if (opGerencia == 3) {
                    exibirRelatorios(scanner);
                }
            }

            if (op == 0) {
                break;
            }
        }
    }

    public void exibirRelatorios(Scanner scanner) {

        System.out.println("\n=== RELATÓRIOS GERENCIAIS ===");
        System.out.println("1 - Controle diário");
        System.out.println("2 - Gestão de horas");
        System.out.println("3 - Situação do mês");
        Integer opRelatorio = scanner.nextInt();

        switch (opRelatorio) {
            case 1:
                System.out.println("Relatório: Controle diário");
                espelhoPonto(scanner);
                break;

            case 2:
                System.out.println("Relatório: Gestão de horas");
                bancoHoras(scanner);
                break;

            case 3:
                System.out.println("Relatório: Situação do mês");
                situacaoMes(scanner);
                break;

            case 4:
                System.out.println("Saindo...");
                break;

            default:
                System.out.println("Opção inválida!");
        }
    }

    public void espelhoPonto(Scanner scanner){
        scanner.nextLine();
        System.out.print("Digite a matrícula: ");
        String matricula = scanner.nextLine();
        Funcionario f = funcionarioService.buscarPorMatricula(matricula);

        if (f != null) {
            System.out.println("\nFuncionário localizado:");
            System.out.println("Nome: " + f.getNome());
            System.out.println("Matrícula: " + f.getMatricula());

            List<ControleDiarioDTO> list = relatorioService.gerarRelatorioControleDiario();
            list.forEach(System.out::println);
        } else {
            System.out.println("Funcionário não encontrado.");
        }
    }
    public void bancoHoras(Scanner scanner){
        System.out.print("Digite a matrícula: ");
        String matricula = scanner.nextLine();
        Funcionario f = funcionarioService.buscarPorMatricula(matricula);
        if (f != null) {
            System.out.println("\nFuncionário localizado:");
            System.out.println("Nome: " + f.getNome());
            System.out.println("Matrícula: " + f.getMatricula());
            CompetenciaDTO competencia = solicitarCompetencia(scanner);
            List<GestaoHorasDTO> list = relatorioService.gerarRelatorioGestaoHoras(competencia.getAno(), competencia.getMes());
            list.forEach(System.out::println);

        } else {
            System.out.println("Funcionário não encontrado.");
        }

    }
    public void situacaoMes(Scanner scanner){
        CompetenciaDTO competencia = solicitarCompetencia(scanner);
        List<SituacaoMensalDTO> list = relatorioService.gerarRelatorioMensal(competencia.getAno(), competencia.getMes());
        list.forEach(System.out::println);
    }

    public CompetenciaDTO solicitarCompetencia(Scanner scanner) {

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

                return new CompetenciaDTO(mes, ano);

            } catch (Exception e) {
                System.out.println("Competência inválida! Use MM/AAAA (ex: 03/2026)");
            }
        }
    }
}