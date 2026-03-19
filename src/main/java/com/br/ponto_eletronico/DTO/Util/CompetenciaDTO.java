package com.br.ponto_eletronico.DTO.Util;

public class CompetenciaDTO {
    private final int mes;
    private final int ano;

    public CompetenciaDTO(int mes, int ano) {
        this.mes = mes;
        this.ano = ano;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }
}
