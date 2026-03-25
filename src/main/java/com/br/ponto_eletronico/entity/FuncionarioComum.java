package com.br.ponto_eletronico.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "FUNCIONARIO_COMUM")
@PrimaryKeyJoinColumn(name = "ID")
public class FuncionarioComum extends Funcionario {

    @ManyToOne
    @JoinColumn(name = "GESTOR_ID", nullable = false)
    private Gestor gestor;

    public Gestor getGestor() {
        return gestor;
    }

    public void setGestor(Gestor gestor) {
        this.gestor = gestor;
    }
}
