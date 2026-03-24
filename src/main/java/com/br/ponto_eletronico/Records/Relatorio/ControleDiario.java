package com.br.ponto_eletronico.Records.Relatorio;

import com.br.ponto_eletronico.Records.Relatorio.MarcacaoPonto;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record ControleDiario(
        String nomeFuncionario,
        String matricula,
        LocalDate data,
        List<MarcacaoPonto> marcacoes,
        double horasTrabalhadas,
        double horasExtras
) {

    public ControleDiario {
        List<MarcacaoPonto> listaOrdenada =
                marcacoes == null
                        ? List.of()
                        : marcacoes.stream()
                        .sorted(Comparator.comparing(MarcacaoPonto::horario))
                        .toList();

        long totalSegundos = calcularSegundos(listaOrdenada);
        double horas = totalSegundos / 3600.0;

        marcacoes = listaOrdenada;
        horasTrabalhadas = horas;
        horasExtras = horas > 6 ? horas - 6 : 0;
    }

    private static long calcularSegundos(List<MarcacaoPonto> marcacoes) {
        LocalDateTime inicio = null;
        long total = 0;

        for (MarcacaoPonto m : marcacoes) {
            switch (m.tipo()) {
                case ENTRADA, VOLTA_INTERVALO -> inicio = m.horario();
                case SAIDA, SAIDA_INTERVALO -> {
                    if (inicio != null) {
                        total += Duration.between(inicio, m.horario()).getSeconds();
                        inicio = null;
                    }
                }
            }
        }
        return total;
    }

    public ControleDiario adicionarMarcacao(MarcacaoPonto marcacao) {
        List<MarcacaoPonto> novaLista = new ArrayList<>(this.marcacoes);
        novaLista.add(marcacao);
        return new ControleDiario(
                nomeFuncionario,
                matricula,
                data,
                novaLista,
                0,
                0
        );
    }

    @Override
    public String toString() {
        DateTimeFormatter dataFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm:ss");

        StringBuilder sb = new StringBuilder();

        sb.append("========================================\n");
        sb.append("Funcionário: ").append(nomeFuncionario).append("\n");
        sb.append("Matrícula  : ").append(matricula).append("\n");
        sb.append("Data       : ")
                .append(data != null ? data.format(dataFmt) : "-")
                .append("\n");

        sb.append("\nMarcações:\n");

        if (marcacoes.isEmpty()) {
            sb.append("  Nenhuma marcação registrada\n");
        } else {
            for (MarcacaoPonto m : marcacoes) {
                sb.append("  ")
                        .append(m.horario().format(horaFmt))
                        .append(" - ")
                        .append(m.tipo())
                        .append("\n");
            }
        }

        sb.append("\nResumo do Dia:\n");
        sb.append("  Horas Trabalhadas: ")
                .append(String.format("%.2f", horasTrabalhadas))
                .append(" h\n");

        sb.append("  Horas Extras     : ")
                .append(String.format("%.2f", horasExtras))
                .append(" h\n");

        sb.append("========================================");

        return sb.toString();
    }
}