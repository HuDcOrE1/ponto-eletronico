package com.br.ponto_eletronico.DTO.Relatorio;

public class GestaoHorasDTO {

    private String nomeFuncionario;
    private String matricula;

    private double saldoHoras;
    private double horasExtras;
    private double horasDevidas;

    @Override
    public String toString() {
        return nomeFuncionario + " | " +
                matricula + " | Saldo: " + saldoHoras +
                " | Extras: " + horasExtras +
                " | Devidas: " + horasDevidas;
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

    public double getSaldoHoras() {
        return saldoHoras;
    }

    public void setSaldoHoras(double saldoHoras) {
        this.saldoHoras = saldoHoras;
    }

    public double getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(double horasExtras) {
        this.horasExtras = horasExtras;
    }

    public double getHorasDevidas() {
        return horasDevidas;
    }

    public void setHorasDevidas(double horasDevidas) {
        this.horasDevidas = horasDevidas;
    }
}