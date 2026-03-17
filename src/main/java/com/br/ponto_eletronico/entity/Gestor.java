package com.br.ponto_eletronico.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "GESTOR")
@PrimaryKeyJoinColumn(name = "ID")
public class Gestor extends Funcionario {

    public Gestor() {
    }

}