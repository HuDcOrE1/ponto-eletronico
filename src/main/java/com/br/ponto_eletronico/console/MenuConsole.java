package com.br.ponto_eletronico.console;


import java.awt.image.renderable.RenderableImage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.entity.FuncionarioComum;
import com.br.ponto_eletronico.entity.Gestor;
import com.br.ponto_eletronico.entity.RegistroPonto;
import com.br.ponto_eletronico.exception.AutenticacaoException;
import com.br.ponto_eletronico.exception.RegraPontoException;
import com.br.ponto_eletronico.repository.RegistroPontoRepository;
import com.br.ponto_eletronico.service.AutenticacaoService;
import com.br.ponto_eletronico.service.FuncionarioService;
import com.br.ponto_eletronico.service.InconsistenciaService;
import com.br.ponto_eletronico.service.PontoService;
import jdk.swing.interop.SwingInterOpUtils;

public class MenuConsole {

    private Scanner scanner = new Scanner(System.in);

    private AutenticacaoService authService = new AutenticacaoService();
    private PontoService pontoService = new PontoService();
    private InconsistenciaService inconsistenciaService =
            new InconsistenciaService();
    private FuncionarioService funcionarioService = new FuncionarioService();

    public void iniciar() {
        System.out.println("=== SISTEMA DE PONTO ===");

        while (true) {
            System.out.println("=== LOGIN ===");
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
                System.out.println("Tente novamente.\n");
            }
        }
    }

    private void menu(Funcionario funcionario) {

        while (true) {

            System.out.println("\n1 - Bater ponto");
            System.out.println("2 - Consultar inconsistências");
            System.out.println("3 - Consultar ponto");
            if (funcionario.isGestor())
                System.out.println("4 - Gerência de ponto");
            System.out.println("0 - Sair");

            int op = scanner.nextInt();
            scanner.nextLine();

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

            if(op == 3) {
                System.out.println("Digite o mês e o ano que deseja consultar (MM/yyyy):");
                String mesAno = scanner.next();
                if(mesAno.matches("^(0[1-9]|1[0-2])\\/\\d{4}$")){
                    Map<String, List<RegistroPonto>> pontoAgrupadosPorDia = pontoService.listarRegistroPontoPorFuncionario(funcionario, mesAno);
                    pontoAgrupadosPorDia.forEach((dataPonto, listaPonto) -> {
                        System.out.println();
                        System.out.println("Data - " + dataPonto);
                        System.out.println("|ENTRADA      |SAIDA INT    |VOLTA INT    |SAIDA       ");
                        for(RegistroPonto horario: listaPonto) {
                            System.out.print(horario.getHorario().format(DateTimeFormatter.ofPattern("|hh:mm:ss     ")));
                        }
                        System.out.println();
                    });
                } else {
                    System.out.println("O formato de dado repassado é inválido");
                }

            }

            if (op == 4 && funcionario instanceof Gestor gestor) {
                Gestor gestorComEquipe = funcionarioService.buscarGestorComEquipe(gestor.getId());

                GerenciaConsole gerenciaConsole =
                        new GerenciaConsole(scanner, funcionarioService, inconsistenciaService, gestorComEquipe);

                gerenciaConsole.menu();
            }

            //Descomentar apenas para gerar massa de dados
            /*if (op == 5) {

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