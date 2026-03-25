package com.br.ponto_eletronico.console;

import com.br.ponto_eletronico.config.JPAUtil;
import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.service.PontoService;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class GeradorPontos {
    public static void executar() {

        EntityManager em = JPAUtil.getEntityManager();
        PontoService service = new PontoService();
        Random random = new Random();

        try {

            List<Funcionario> funcionarios = em.createQuery(
                            "SELECT f FROM Funcionario f WHERE f.matricula NOT IN :mats",
                            Funcionario.class
                    )
                    .setParameter("mats", List.of("1001", "1002", "1003", "1004", "1005"))
                    .getResultList();

            for (Funcionario f : funcionarios) {
                for (int d = 1; d <= 90; d++) {

                    LocalDate data = LocalDate.now().minusDays(d);

                    // pula fim de semana
                    if (data.getDayOfWeek().getValue() >= 6) continue;

                    // entrada: 07h - 09h
                    LocalDateTime entrada = data.atTime(
                            8 + random.nextInt(3),
                            random.nextInt(60)
                    );

                    // saída intervalo (~2h depois)
                    LocalDateTime saidaIntervalo =
                            entrada.plusHours(2).plusMinutes(random.nextInt(10));

                    int tipoIntervalo = random.nextInt(100);

                    int minutosIntervalo;

                    if (tipoIntervalo < 70) {
                        // Normal (30 a 120 min)
                        minutosIntervalo = 30 + random.nextInt(91);

                    } else if (tipoIntervalo < 85) {
                        // Menor que 30 min
                        minutosIntervalo = 5 + random.nextInt(25);

                    } else {
                        // Maior que 120 min
                        minutosIntervalo = 121 + random.nextInt(60);
                    }

                    // intervalo (30 min a 2h)
                    LocalDateTime voltaIntervalo =
                            saidaIntervalo.plusMinutes(minutosIntervalo);

                    // saída final (~4h depois)
                    LocalDateTime saidaFinal =
                            voltaIntervalo.plusHours(4)
                                    .plusMinutes(random.nextInt(10));

                    service.baterPontoDump(f, entrada);
                    service.baterPontoDump(f, saidaIntervalo);
                    service.baterPontoDump(f, voltaIntervalo);
                    service.baterPontoDump(f, saidaFinal);

                    System.out.println("Dia " + data + " gerado");
                }
            }

            System.out.println("Pontos gerados com sucesso!");

        } finally {
            em.close();
        }
    }
}