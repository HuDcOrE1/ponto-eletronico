package com.br.ponto_eletronico.DTO.Relatorio;

public class SituacaoMensalDTO {

    private String nomeFuncionario;
    private String matricula;

    private double horasEsperadas;
    private double horasTrabalhadas;
    private double saldo;

    private int diasTrabalhados;

    @Override
    public String toString() {
        return nomeFuncionario + " | " +
                matricula + " | Horas Esperadas: " + horasEsperadas +
                " | Horas Trabalhadas: " + horasTrabalhadas +
                " | Saldo: " + saldo +
                " | Dias Trabalhados: " + diasTrabalhados;
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

    public double getHorasEsperadas() {
        return horasEsperadas;
    }

    public void setHorasEsperadas(double horasEsperadas) {
        this.horasEsperadas = horasEsperadas;
    }

    public double getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(double horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getDiasTrabalhados() {
        return diasTrabalhados;
    }

    public void setDiasTrabalhados(int diasTrabalhados) {
        this.diasTrabalhados = diasTrabalhados;
    }
}