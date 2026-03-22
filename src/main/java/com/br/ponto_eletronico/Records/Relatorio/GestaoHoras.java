package com.br.ponto_eletronico.Records.Relatorio;

public record GestaoHoras(
        String nomeFuncionario,
        String matricula,
        double saldoHoras,
        double horasExtras,
        double horasDevidas
) {

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Funcionário   : ").append(nomeFuncionario).append("\n")
                .append("Matrícula     : ").append(matricula).append("\n")
                .append("Saldo Horas   : ").append(formatarSaldo(saldoHoras)).append("\n")
                .append("Horas Extras  : ").append(formatarSaldo(horasExtras)).append("\n")
                .append("Horas Devidas : ").append(formatarSaldo(horasDevidas))
                .toString();
    }

    private static String formatarSaldo(double valor) {
        valor = Math.abs(valor);

        int horas = (int) valor;
        int minutos = (int) Math.round((valor - horas) * 60);

        if (minutos == 60) {
            horas++;
            minutos = 0;
        }

        return horas + "h " + minutos + "min";
    }

    public String saldoHorasFormatado() {
        return formatarSaldo(saldoHoras);
    }

    public String horasExtrasFormatado() {
        return formatarSaldo(horasExtras);
    }

    public String horasDevidasFormatado() {
        return formatarSaldo(horasDevidas);
    }
}