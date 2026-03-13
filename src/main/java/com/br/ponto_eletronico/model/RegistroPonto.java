package com.br.ponto_eletronico.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class RegistroPonto {

    @Id
    @GeneratedValue
    private Long id;

    private Long funcionarioId;

    private LocalDateTime dataHora;

    private String tipo;

}