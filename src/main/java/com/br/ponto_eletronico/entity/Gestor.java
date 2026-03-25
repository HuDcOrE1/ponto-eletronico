package com.br.ponto_eletronico.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GESTOR")
@PrimaryKeyJoinColumn(name = "ID")
public class Gestor extends Funcionario {

    @OneToMany(mappedBy = "gestor")
    private List<FuncionarioComum> equipe = new ArrayList<>();

    public List<FuncionarioComum> getEquipe() {
        return equipe;
    }

    public void adicionarFuncionario(FuncionarioComum funcionario) {
        funcionario.setGestor(this);
        equipe.add(funcionario);
    }

    public void removerFuncionario(FuncionarioComum funcionario) {
        funcionario.setGestor(null);
        equipe.remove(funcionario);
    }
}