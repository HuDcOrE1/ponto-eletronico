package com.br.ponto_eletronico.service;

import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.exception.AutenticacaoException;
import com.br.ponto_eletronico.repository.FuncionarioRepository;

public class AutenticacaoService {

    private FuncionarioRepository repository = new FuncionarioRepository();

    public Funcionario login(String matricula, String senha) {

        Funcionario funcionario = repository.buscarPorMatricula(matricula);
        System.out.println("Teste->>>>>"+ funcionario);

        if (funcionario == null) {
            throw new AutenticacaoException("Funcionário não encontrado");
        }

        if (!funcionario.getSenha().equals(senha)) {
            throw new AutenticacaoException("Senha inválida");
        }

        return funcionario;
    }

}