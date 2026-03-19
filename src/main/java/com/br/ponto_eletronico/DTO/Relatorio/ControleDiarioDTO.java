package com.br.ponto_eletronico.DTO.Relatorio;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ControleDiarioDTO {

    private String nomeFuncionario;
    private String matricula;

    private LocalDate data;

    private LocalDateTime entrada;
    private LocalDateTime saida;

    private double horasTrabalhadas;
    private double horasExtras;

    @Override
    public String toString() {

        DateTimeFormatter dataFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");

        String dataFormatada = data != null ? data.format(dataFmt) : "-";
        String entradaFormatada = entrada != null ? entrada.format(horaFmt) : "-";
        String saidaFormatada = saida != null ? saida.format(horaFmt) : "-";

        double horas = 0;
        double extras = 0;

        if (entrada != null && saida != null) {
            long minutos = Duration.between(entrada, saida).toMinutes();
            horas = minutos / 60.0;
            extras = horas > 8 ? horas - 8 : 0;
        }

        return "Funcionário: " + nomeFuncionario +
                " | Matrícula: " + matricula +
                " | Data: " + dataFormatada +
                " | Entrada: " + entradaFormatada +
                " | Saída: " + saidaFormatada +
                " | Horas Trabalhadas: " + String.format("%.2f", horas) +
                " | Horas Extras: " + String.format("%.2f", extras);
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    public double getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(double horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public double getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(double horasExtras) {
        this.horasExtras = horasExtras;
    }
}