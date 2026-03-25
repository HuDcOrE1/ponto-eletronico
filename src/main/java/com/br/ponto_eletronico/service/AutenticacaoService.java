package com.br.ponto_eletronico.service;

import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.exception.AutenticacaoException;
import com.br.ponto_eletronico.repository.FuncionarioRepository;

public class AutenticacaoService {

    private FuncionarioRepository repository = new FuncionarioRepository();

    public Funcionario login(String matricula, String senha) {

        Funcionario funcionario = repository.login(matricula)
                .orElseThrow(() -> new AutenticacaoException("Usuario ou Senha inválida"));

        if (!funcionario.getSenha().equals(senha)) {
            throw new AutenticacaoException("Usuario ou Senha inválida");
        }

        return funcionario;
    }

}