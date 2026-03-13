package com.br.ponto_eletronico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Funcionario {

    @Id
    @GeneratedValue
    private Long id;

    private String matricula;
    private String senha;
    private String nome;

}
