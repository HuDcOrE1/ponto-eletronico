package com.br.ponto_eletronico.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REGISTRO_PONTO")
public class RegistroPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FUNCIONARIO_ID")
    private Funcionario funcionario;

    @Column(name = "HORARIO")
    private LocalDateTime horario;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO")
    private TipoRegistro tipo;

    public RegistroPonto() {
    }

    public RegistroPonto(Funcionario funcionario, LocalDateTime horario, TipoRegistro tipo) {
        this.funcionario = funcionario;
        this.horario = horario;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public TipoRegistro getTipo() {
        return tipo;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public void setTipo(TipoRegistro tipo) {
        this.tipo = tipo;
    }
}