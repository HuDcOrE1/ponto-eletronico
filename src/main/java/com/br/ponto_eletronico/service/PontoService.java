package com.br.ponto_eletronico.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.br.ponto_eletronico.entity.*;
import com.br.ponto_eletronico.repository.InconsistenciaRepository;
import com.br.ponto_eletronico.repository.RegistroPontoRepository;

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
            case 0: tipo = TipoRegistro.ENTRADA; break;
            case 1: tipo = TipoRegistro.SAIDA_INTERVALO; break;
            case 2: tipo = TipoRegistro.VOLTA_INTERVALO; break;
            case 3: tipo = TipoRegistro.SAIDA; break;
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

        // Regra 3: jornada maior que 6h
        if (registros.size() >= 1) {

            RegistroPonto entrada = registros.get(0);

            Duration jornada =
                    Duration.between(entrada.getHorario(), agora);

            if (jornada.toHours() > 6) {
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
}