package com.br.ponto_eletronico.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "FUNCIONARIO")
@Inheritance(strategy = InheritanceType.JOINED)
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MATRICULA")
    private String matricula;

    @Column(name = "SENHA")
    private String senha;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "GESTOR")
    private boolean gestor;

    public Funcionario() {
    }

    public Long getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public boolean isGestor() {
        return gestor;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setGestor(boolean gestor) {
        this.gestor = gestor;
    }
}