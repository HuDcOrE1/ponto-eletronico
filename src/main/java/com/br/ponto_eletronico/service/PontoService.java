package com.br.ponto_eletronico.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.br.ponto_eletronico.config.SqlFileLogger;
import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.entity.RegistroPonto;
import com.br.ponto_eletronico.entity.TipoRegistro;
import com.br.ponto_eletronico.exception.RegraPontoException;
import com.br.ponto_eletronico.repository.RegistroPontoRepository;

public class PontoService {

    private RegistroPontoRepository repository =
            new RegistroPontoRepository();

    public void baterPonto(Funcionario funcionario)
            throws RegraPontoException {

        List<RegistroPonto> registros =
                repository.buscarRegistrosHoje(funcionario);

        TipoRegistro tipo;

        switch (registros.size()) {

            case 0:
                tipo = TipoRegistro.ENTRADA;
                break;

            case 1:

                RegistroPonto entrada = registros.get(0);

                Duration diferenca = Duration.between(
                        entrada.getHorario(),
                        LocalDateTime.now()
                );

                long horas = diferenca.toHours();
                long minutos = diferenca.toMinutes();
                System.out.println("Minutos: " + minutos);

                // if (horas < 2) {
                if (minutos < 1) {
                    throw new RegraPontoException(
                            "Intervalo mínimo de 2 horas após a entrada."
                    );
                }

                // if (horas > 4) {
                // if (minutos > 3) { Conversar com a galera pq essa regra não faz sentido
                //     throw new RegraPontoException(
                //             "Intervalo máximo de 4 horas após a entrada."
                //     );
                // }

                tipo = TipoRegistro.SAIDA_INTERVALO;
                break;

            case 2:
                tipo = TipoRegistro.VOLTA_INTERVALO;
                break;

            case 3:
                tipo = TipoRegistro.SAIDA;
                break;

            default:
                throw new RegraPontoException(
                        "Todos os pontos do dia já foram registrados."
                );
        }

        LocalDateTime agora = LocalDateTime.now();

        RegistroPonto registro = new RegistroPonto();
        registro.setFuncionario(funcionario);
        registro.setHorario(agora);
        registro.setTipo(tipo);

        repository.salvar(registro);

        SqlFileLogger.registrarPonto(funcionario, agora, tipo);

        System.out.println("Ponto registrado: " + tipo);
    }
}