package com.br.ponto_eletronico.Records.Relatorio;

public record SituacaoMensal(
        String nomeFuncionario,
        String matricula,
        double horasEsperadas,
        double horasTrabalhadas,
        long quantidadeInconsistencias,
        double saldo,
        int diasTrabalhados
) {

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Funcionário            : ").append(nomeFuncionario).append("\n")
                .append("Matrícula              : ").append(matricula).append("\n")
                .append("Horas Esperadas        : ").append(formatarHoras(horasEsperadas)).append("\n")
                .append("Horas Trabalhadas      : ").append(formatarHoras(horasTrabalhadas)).append("\n")
                .append("Saldo de Horas         : ").append(formatarSaldo(saldo)).append("\n")
                .append("Dias Trabalhados       : ").append(diasTrabalhados).append("\n")
                .append("Inconsistências no Mês : ").append(quantidadeInconsistencias)
                .toString();
    }

    private static String formatarHoras(double valor) {
        valor = Math.abs(valor);

        int horas = (int) valor;
        int minutos = (int) Math.round((valor - horas) * 60);

        if (minutos == 60) {
            horas++;
            minutos = 0;
        }

        return horas + "h " + minutos + "min";
    }

    private static String formatarSaldo(double valor) {
        boolean negativo = valor < 0;
        String horasFormatadas = formatarHoras(valor);
        return negativo ? "-" + horasFormatadas : "+" + horasFormatadas;
    }

    public String horasEsperadasFormatadas() {
        return formatarHoras(horasEsperadas);
    }

    public String horasTrabalhadasFormatadas() {
        return formatarHoras(horasTrabalhadas);
    }

    public String saldoFormatado() {
        return formatarSaldo(saldo);
    }
}