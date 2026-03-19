package com.br.ponto_eletronico.service;

import com.br.ponto_eletronico.DTO.Relatorio.ControleDiarioDTO;
import com.br.ponto_eletronico.DTO.Relatorio.GestaoHorasDTO;
import com.br.ponto_eletronico.DTO.Relatorio.SituacaoMensalDTO;
import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.entity.RegistroPonto;
import com.br.ponto_eletronico.repository.RelatorioRepository;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class RelatorioService {
    private RelatorioRepository repository =
            new RelatorioRepository();


    public List<ControleDiarioDTO> gerarRelatorioControleDiario() {

        List<ControleDiarioDTO> relatorio = new ArrayList<>();

        List<Funcionario> funcionarios = repository.buscarTodosFuncionarios();

        for (Funcionario f : funcionarios) {
            LocalDateTime agora = LocalDateTime.now();

            List<RegistroPonto> registros =
                    repository.buscarPorFuncionario(f.getMatricula(), agora.getYear(), agora.getMonthValue(), agora.getDayOfMonth());

            registros.sort(Comparator.comparing(RegistroPonto::getHorario));

            for (int i = 0; i < registros.size(); i += 2) {

                if (i + 1 < registros.size()) {

                    LocalDateTime entrada = registros.get(i).getHorario();
                    LocalDateTime saida = registros.get(i + 1).getHorario();

                    double horas = Duration.between(entrada, saida).toMinutes() / 60.0;

                    ControleDiarioDTO dto = new ControleDiarioDTO();
                    dto.setNomeFuncionario(f.getNome());
                    dto.setMatricula(f.getMatricula());
                    dto.setData(entrada.toLocalDate());
                    dto.setEntrada(entrada);
                    dto.setSaida(saida);
                    dto.setHorasTrabalhadas(horas);
                    dto.setHorasExtras(horas > 6 ? horas - 6 : 0);

                    relatorio.add(dto);
                }
            }
        }

        return relatorio;
    }

    public List<GestaoHorasDTO> gerarRelatorioGestaoHoras(int ano, int mes) {

        List<GestaoHorasDTO> relatorio = new ArrayList<>();

        List<Funcionario> funcionarios = repository.buscarTodosFuncionarios();

        for (Funcionario f : funcionarios) {

            List<RegistroPonto> registros =
                    repository.buscarPorFuncionario(f.getMatricula(), ano, mes);

            double horas = calcularHoras(registros);

            double horasEsperadas = calcularHorasEsperadas(6, ano, mes);
            double saldo = horas - horasEsperadas;

            GestaoHorasDTO dto = new GestaoHorasDTO();
            dto.setNomeFuncionario(f.getNome());
            dto.setMatricula(f.getMatricula());
            dto.setSaldoHoras(saldo);
            dto.setHorasExtras(saldo > 0 ? saldo : 0);
            dto.setHorasDevidas(saldo < 0 ? Math.abs(saldo) : 0);

            relatorio.add(dto);
        }

        return relatorio;
    }

    public List<SituacaoMensalDTO> gerarRelatorioMensal(int ano, int mes) {

        List<SituacaoMensalDTO> relatorio = new ArrayList<>();

        List<Funcionario> funcionarios = repository.buscarTodosFuncionarios();

        for (Funcionario f : funcionarios) {

            List<RegistroPonto> registros =
                    repository.buscarPorFuncionario(f.getMatricula(), ano, mes);

            double horasTrabalhadas = calcularHoras(registros);

            double horasEsperadas = calcularHorasEsperadas(6, ano, mes);

            SituacaoMensalDTO dto = new SituacaoMensalDTO();
            dto.setNomeFuncionario(f.getNome());
            dto.setMatricula(f.getMatricula());
            dto.setHorasEsperadas(horasEsperadas);
            dto.setHorasTrabalhadas(horasTrabalhadas);
            dto.setSaldo(horasTrabalhadas - horasEsperadas);
            dto.setDiasTrabalhados(contarDias(registros));

            relatorio.add(dto);
        }

        return relatorio;
    }

    public String gerarCsv(Integer op, int ano, int mes) {
        StringBuilder csv = new StringBuilder();

        switch (op) {
            case 1:
                List<ControleDiarioDTO> relatorioCD = gerarRelatorioControleDiario();
                csv.append("Nome;Data;Horas\n");
                for (ControleDiarioDTO d : relatorioCD) {
                    csv.append(d.getNomeFuncionario())
                            .append(";")
                            .append(d.getData())
                            .append(";")
                            .append(d.getHorasTrabalhadas())
                            .append("\n");
                }
                break;

            case 2:
                List<GestaoHorasDTO> relatorioGH = gerarRelatorioGestaoHoras(ano, mes);
                csv.append("Nome;Saldo\n");
                for (GestaoHorasDTO d : relatorioGH) {
                    csv.append(d.getNomeFuncionario())
                            .append(";")
                            .append(d.getSaldoHoras())
                            .append("\n");
                }
                break;

            case 3:
                List<SituacaoMensalDTO> relatorioSM = gerarRelatorioMensal(ano, mes);
                csv.append("Nome;Esperado;Trabalhado;Saldo\n");
                for (SituacaoMensalDTO d : relatorioSM) {
                    csv.append(d.getNomeFuncionario())
                            .append(";")
                            .append(d.getHorasEsperadas())
                            .append(";")
                            .append(d.getHorasTrabalhadas())
                            .append(";")
                            .append(d.getSaldo())
                            .append("\n");
                }
                break;

            default:
                throw new IllegalArgumentException("Opção inválida!");
        }

        return csv.toString();
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