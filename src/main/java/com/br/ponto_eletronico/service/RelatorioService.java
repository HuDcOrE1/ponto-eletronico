package com.br.ponto_eletronico.service;

import com.br.ponto_eletronico.Records.Relatorio.ControleDiario;
import com.br.ponto_eletronico.Records.Relatorio.ControleDiario;
import com.br.ponto_eletronico.Records.Relatorio.GestaoHoras;
import com.br.ponto_eletronico.Records.Relatorio.MarcacaoPonto;
import com.br.ponto_eletronico.Records.Relatorio.MarcacaoPonto;
import com.br.ponto_eletronico.Records.Relatorio.SituacaoMensal;
import com.br.ponto_eletronico.entity.*;
import com.br.ponto_eletronico.repository.InconsistenciaRepository;
import com.br.ponto_eletronico.repository.RelatorioRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class RelatorioService {
    private RelatorioRepository repository =
            new RelatorioRepository();

    private InconsistenciaService inconsistenciaService =
            new InconsistenciaService();

    public ControleDiario gerarRelatorioControleDiario(FuncionarioComum funcionario) {

        LocalDate hoje = LocalDate.now();
        LocalDateTime now = LocalDate.now().atStartOfDay();
        List<Inconsistencia> inconsistencias = inconsistenciaService.getPorFuncionarioData(funcionario, now);
        List<RegistroPonto> registros =
                repository.buscarPorFuncionario(
                        funcionario,
                        hoje.getYear(),
                        hoje.getMonthValue(),
                        hoje.getDayOfMonth()
                );

        ControleDiario controle = new ControleDiario(
                funcionario.getNome(),
                funcionario.getMatricula(),
                hoje,
                inconsistencias,
                List.of(),
                0,
                0
        );

        registros.sort(Comparator.comparing(RegistroPonto::getHorario));

        for (RegistroPonto registroPonto : registros) {
            controle = controle.comMarcacao(
                    new MarcacaoPonto(registroPonto.getHorario(), registroPonto.getTipo())
            );
        }

        return controle;
    }

    public List<GestaoHoras> gerarRelatorioGestaoHoras(FuncionarioComum funcionario, int ano, int mes) {
        List<GestaoHoras> relatorio = new ArrayList<>();

        List<RegistroPonto> registros =
                repository.buscarPorFuncionario(funcionario.getId(), ano, mes);

        double horas = calcularHoras(registros);

        double horasEsperadas = calcularHorasEsperadas(6, ano, mes);
        double saldo = horas;

        GestaoHoras gestao = new GestaoHoras(
                funcionario.getNome(),
                funcionario.getMatricula(),
                saldo,
                saldo > horasEsperadas ? saldo - horasEsperadas : 0,
                saldo < horasEsperadas ? horasEsperadas - saldo : 0
        );

        relatorio.add(gestao);

        return relatorio;
    }

    public List<SituacaoMensal> gerarRelatorioMensal(int ano, int mes) {

        List<SituacaoMensal> relatorio = new ArrayList<>();

        List<Funcionario> funcionarios = repository.buscarTodosFuncionarios();

        for (Funcionario funcionario : funcionarios) {
            List<RegistroPonto> registros = repository.buscarPorFuncionario(funcionario.getId(), ano, mes);
            double horasTrabalhadas = calcularHoras(registros);
            double horasEsperadas = calcularHorasEsperadas(6, ano, mes);
            long inconsistencias = repository.contarInconsistencias(funcionario.getId(), ano, mes);


            SituacaoMensal situacao = new SituacaoMensal(
                    funcionario.getNome(),
                    funcionario.getMatricula(),
                    horasEsperadas,
                    horasTrabalhadas,
                    inconsistencias,
                    horasTrabalhadas - horasEsperadas,
                    contarDias(registros)
            );

            relatorio.add(situacao);
        }

        return relatorio;
    }

    public String gerarCsv(Integer op, int ano, int mes, Path ABSOLUTE_PATH, FuncionarioComum funcionario) {
        StringBuilder csv = new StringBuilder();
        String nomeArquivo;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");

        switch (op) {

            case 1:
                ControleDiario relatorioCD = gerarRelatorioControleDiario(funcionario);
                csv.append("Funcionario;Matricula;Data;Horario;Tipo\n");
                DateTimeFormatter dataFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm:ss");

                if (!relatorioCD.marcacoes().isEmpty()) {
                    for (MarcacaoPonto m : relatorioCD.marcacoes()) {
                        csv.append(relatorioCD.nomeFuncionario()).append(";")
                                .append(relatorioCD.matricula()).append(";")
                                .append(relatorioCD.data().format(dataFmt)).append(";")
                                .append(m.horario().format(horaFmt)).append(";")
                                .append(m.tipo()).append(";")
                                .append("\n");
                    }
                }

                if (!relatorioCD.inconsistencias().isEmpty()) {
                    csv.append("Inconsistências: \n");
                    for (Inconsistencia inconsistencia : relatorioCD.inconsistencias()) {
                        csv.append(inconsistencia.getDescricao())
                                .append("\n");
                    }
                }

                nomeArquivo = "relatorio_controle_diario_" + LocalDate.now().format(fmt) + relatorioCD.matricula() + ".csv";
                break;

            case 2:
                List<GestaoHoras> relatorioGH = gerarRelatorioGestaoHoras(funcionario, ano, mes);
                csv.append("Funcionario;Matricula;Saldo Horas;Horas Extras;Horas Devidas\n");

                for (GestaoHoras gestaoHoras : relatorioGH) {
                    csv.append(gestaoHoras.nomeFuncionario()).append(";")
                            .append(gestaoHoras.matricula()).append(";")
                            .append(gestaoHoras.saldoHorasFormatado()).append(";")
                            .append(gestaoHoras.horasExtrasFormatado()).append(";")
                            .append(gestaoHoras.horasDevidasFormatado())
                            .append("\n");
                }

                nomeArquivo = "relatorio_gestao_horas_" + ano + "_" + mes + ".csv";
                break;

            case 3:
                List<SituacaoMensal> relatorioSM = gerarRelatorioMensal(ano, mes);
                csv.append("Funcionario;Matricula;Horas Esperadas;Horas Trabalhadas;Saldo;Dias Trabalhados;Inconsistencias\n");

                for (SituacaoMensal d : relatorioSM) {
                    csv.append(d.nomeFuncionario()).append(";")
                            .append(d.matricula()).append(";")
                            .append(d.horasEsperadasFormatadas()).append(";")
                            .append(d.horasTrabalhadasFormatadas()).append(";")
                            .append(d.saldoFormatado()).append(";")
                            .append(d.diasTrabalhados()).append(";")
                            .append(d.quantidadeInconsistencias())
                            .append("\n");
                }

                nomeArquivo = "relatorio_mensal_" + ano + "_" + mes + ".csv";
                break;

            default:
                throw new IllegalArgumentException("Opção inválida!");
        }

        try {
            Files.createDirectories(ABSOLUTE_PATH);

            Path caminhoArquivo = ABSOLUTE_PATH.resolve(nomeArquivo);

            Files.write(
                    caminhoArquivo,
                    csv.toString().getBytes(StandardCharsets.UTF_8)
            );

            return "✅ Relatório gerado com sucesso!\n📁 Arquivo salvo em: "
                    + caminhoArquivo.toAbsolutePath();

        } catch (IOException e) {
            return "❌ Erro ao gerar o relatório CSV: " + e.getMessage();
        }
    }

    private double calcularHoras(List<RegistroPonto> registros) {

        double totalHoras = 0;

        registros.sort(Comparator.comparing(RegistroPonto::getHorario));

        for (int i = 0; i < registros.size(); i += 2) {

            if (i + 1 < registros.size()) {

                LocalDateTime entrada = registros.get(i).getHorario();
                LocalDateTime saida = registros.get(i + 1).getHorario();

                long minutos = Duration.between(entrada, saida).toMinutes();

                totalHoras += minutos / 60.0;
            }
        }

        return totalHoras;
    }

    private int contarDias(List<RegistroPonto> registros) {
        return (int) registros.stream()
                .map(r -> r.getHorario().toLocalDate())
                .distinct()
                .count();
    }

    private double calcularHorasEsperadas(int cargaHorariaDiaria, int ano, int mes) {

        YearMonth yearMonth = YearMonth.of(ano, mes);

        int diasUteis = 0;

        for (int dia = 1; dia <= yearMonth.lengthOfMonth(); dia++) {

            LocalDate data = yearMonth.atDay(dia);

            DayOfWeek diaSemana = data.getDayOfWeek();

            if (diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY) {
                diasUteis++;
            }
        }

        return diasUteis * cargaHorariaDiaria;
    }
}