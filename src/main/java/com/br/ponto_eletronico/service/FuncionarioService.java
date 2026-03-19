package com.br.ponto_eletronico.service;

import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.repository.FuncionarioRepository;

public class FuncionarioService {
    private FuncionarioRepository repository = new FuncionarioRepository();

    public Funcionario buscarPorMatricula(String matricula) {
        return repository.buscarPorMatricula(matricula);
    }
}
