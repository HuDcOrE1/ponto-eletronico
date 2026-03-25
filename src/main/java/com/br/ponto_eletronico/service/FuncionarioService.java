package com.br.ponto_eletronico.service;

import com.br.ponto_eletronico.config.JPAUtil;
import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.entity.FuncionarioComum;
import com.br.ponto_eletronico.entity.Gestor;
import com.br.ponto_eletronico.repository.FuncionarioRepository;
import jakarta.persistence.EntityManager;

public class FuncionarioService {
    private FuncionarioRepository repository = new FuncionarioRepository();

    public FuncionarioComum buscarPorMatricula(String matricula) {
        return repository.buscarPorMatricula(matricula);
    }

    public Gestor buscarGestorComEquipe(Long id) {
        return repository.buscarGestorComEquipe(id);
    }

}
