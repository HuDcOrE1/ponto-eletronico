package com.br.ponto_eletronico.service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.br.ponto_eletronico.entity.*;
import com.br.ponto_eletronico.repository.InconsistenciaRepository;
import com.br.ponto_eletronico.repository.RegistroPontoRepository;
import com.br.ponto_eletronico.exception.RegraPontoException;

public class PontoService {

    private RegistroPontoRepository repository =
            new RegistroPontoRepository();

    private InconsistenciaRepository inconsistenciaRepository =
            new InconsistenciaRepository();

    public void baterPonto(Funcionario funcionario) {

        List<RegistroPonto> registros =
                repository.buscarRegistrosHoje(funcionario);

        TipoRegistro tipo;

        switch (registros.size()) {
            case 0:
                tipo = TipoRegistro.ENTRADA;
                break;
            case 1:
                tipo = TipoRegistro.SAIDA_INTERVALO;
                break;
            case 2:
                tipo = TipoRegistro.VOLTA_INTERVALO;
                break;
            case 3:
                tipo = TipoRegistro.SAIDA;
                break;
            default:
                System.out.println("Limite de registros atingido.");
                return;
        }

        LocalDateTime agora = LocalDateTime.now();

        // =========================
        // REGRAS DE INCONSISTÊNCIA
        // =========================

        if (!registros.isEmpty()) {

            RegistroPonto ultimo =
                    registros.get(registros.size() - 1);

            if (ultimo.getTipo() == TipoRegistro.SAIDA_INTERVALO) {
                Duration diff =
                        Duration.between(ultimo.getHorario(), agora);

                long minutos = diff.toMinutes();

                // Regra 1: menor que 30 min
                if (minutos < 30) {
                    salvarInconsistencia(funcionario,
                            "Intervalo menor que 30 minutos");
                }

                // Regra 2: maior que 2 horas
                if (minutos > 120) {
                    salvarInconsistencia(funcionario,
                            "Intervalo maior que 2 horas");
                }
            }
        }

        if (tipo == TipoRegistro.SAIDA) {
            Duration jornada;

            if (registros.size() >= 3) {
                RegistroPonto entrada = registros.get(0);
                RegistroPonto saidaIntervalo = registros.get(1);
                RegistroPonto voltaIntervalo = registros.get(2);

                Duration antesIntervalo =
                        Duration.between(entrada.getHorario(), saidaIntervalo.getHorario());

                Duration depoisIntervalo =
                        Duration.between(voltaIntervalo.getHorario(), agora);

                jornada = antesIntervalo.plus(depoisIntervalo);
            } else {
                RegistroPonto entrada = registros.get(0);
                jornada = Duration.between(entrada.getHorario(), agora);
            }

            if (jornada.toMinutes() > 370) {
                salvarInconsistencia(funcionario,
                        "Jornada maior que 6 horas");
            }
        }
        // =========================

        RegistroPonto registro = new RegistroPonto();
        registro.setFuncionario(funcionario);
        registro.setHorario(agora);
        registro.setTipo(tipo);

        repository.salvar(registro);

        System.out.println("Ponto registrado: " + tipo);
    }

    private void salvarInconsistencia(Funcionario funcionario,
                                      String descricao) {

        Inconsistencia i = new Inconsistencia();

        i.setFuncionario(funcionario);
        i.setDescricao(descricao);
        i.setHorario(LocalDateTime.now());

        inconsistenciaRepository.salvar(i);

        System.out.println("Inconsistência registrada: " + descricao);
    }

    private void salvarInconsistenciaAntiga(Funcionario funcionario,
                                            String descricao, LocalDateTime horario) {

        Inconsistencia i = new Inconsistencia();

        i.setFuncionario(funcionario);
        i.setDescricao(descricao);
        i.setHorario(horario);

        inconsistenciaRepository.salvar(i);

        System.out.println("Inconsistência registrada: " + descricao);
    }

    public void baterPontoAntigo(Funcionario funcionario, LocalDateTime horario) {
        LocalDate dia = horario.toLocalDate();

        List<RegistroPonto> registros =
                repository.buscarRegistrosDia(funcionario, dia);

        TipoRegistro tipo;

        switch (registros.size()) {
            case 0:
                tipo = TipoRegistro.ENTRADA;
                break;
            case 1:
                tipo = TipoRegistro.SAIDA_INTERVALO;
                break;
            case 2:
                tipo = TipoRegistro.VOLTA_INTERVALO;
                break;
            case 3:
                tipo = TipoRegistro.SAIDA;
                break;
            default:
                return;
        }

        // =========================
        // REGRAS
        // =========================

        if (!registros.isEmpty()) {
            RegistroPonto ultimo = registros.get(registros.size() - 1);

            if (ultimo.getTipo() == TipoRegistro.SAIDA_INTERVALO) {
                Duration diff = Duration.between(ultimo.getHorario(), horario);
                long minutos = diff.toMinutes();

                if (minutos < 0) return;

                if (minutos < 30) {
                    salvarInconsistenciaAntiga(funcionario,
                            "Intervalo menor que 30 minutos", horario);
                }

                if (minutos > 120) {
                    salvarInconsistenciaAntiga(funcionario,
                            "Intervalo maior que 2 horas", horario);
                }
            }
        }

        if (tipo == TipoRegistro.SAIDA) {
            Duration jornada;

            if (registros.size() >= 3) {
                RegistroPonto entrada = registros.get(0);
                RegistroPonto saidaIntervalo = registros.get(1);
                RegistroPonto voltaIntervalo = registros.get(2);

                Duration antesIntervalo =
                        Duration.between(entrada.getHorario(), saidaIntervalo.getHorario());

                Duration depoisIntervalo =
                        Duration.between(voltaIntervalo.getHorario(), horario);

                jornada = antesIntervalo.plus(depoisIntervalo);
            } else {
                RegistroPonto entrada = registros.get(0);
                jornada = Duration.between(entrada.getHorario(), horario);
            }

            if (jornada.toMinutes() > 370) {
                salvarInconsistenciaAntiga(funcionario,
                        "Jornada maior que 6 horas", horario);
            }
        }

        RegistroPonto registro = new RegistroPonto(funcionario, horario, tipo);

        repository.salvar(registro);
    }

    
    public Map<String, List<RegistroPonto>> listarRegistroPontoPorFuncionario(Funcionario funcionario, String mesAno){
        YearMonth mesAnoSelecionado = YearMonth.parse(mesAno, DateTimeFormatter.ofPattern("MM/yyyy"));
        var pontosAgrupadosPorDia = repository.buscarRegistrosPorFuncionario(funcionario).stream().filter(ponto -> {
            YearMonth pontoMesAnoFuncionario = YearMonth.of(ponto.getHorario().getYear(), ponto.getHorario().getMonth());
            return mesAnoSelecionado.equals(pontoMesAnoFuncionario);
        }).collect(Collectors.groupingBy(item -> {
            return item.getHorario().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }));
        return pontosAgrupadosPorDia;

    }
    
    public void resolverInconsistencia(Long idInconsistencia, List<String> horariosStr) throws RegraPontoException {
        Inconsistencia i = inconsistenciaRepository.buscarPorId(idInconsistencia);
        if (i == null) {
            throw new RegraPontoException("Inconsistência não encontrada.");
        }

        if (horariosStr.size() != 4) {
            throw new RegraPontoException("São necessários exatamente 4 horários para o ponto diário completo.");
        }

        Funcionario f = i.getFuncionario();
        LocalDate dataInconsistencia = i.getHorario().toLocalDate();

        List<RegistroPonto> registrosExistentes = repository.buscarRegistrosPorData(f, dataInconsistencia);
        repository.deletarLista(registrosExistentes);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        TipoRegistro[] tipos = {TipoRegistro.ENTRADA, TipoRegistro.SAIDA_INTERVALO, TipoRegistro.VOLTA_INTERVALO, TipoRegistro.SAIDA};

        for (int j = 0; j < 4; j++) {
            LocalTime hora = LocalTime.parse(horariosStr.get(j), timeFormatter);
            RegistroPonto novoRegistro = new RegistroPonto();
            novoRegistro.setFuncionario(f);
            novoRegistro.setHorario(LocalDateTime.of(dataInconsistencia, hora));
            novoRegistro.setTipo(tipos[j]);
            repository.salvar(novoRegistro);
        }

        inconsistenciaRepository.deletar(i);
        System.out.println("Inconsistência resolvida e pontos sobresscritos.");
    }
}