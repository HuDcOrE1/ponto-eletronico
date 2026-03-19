package com.br.ponto_eletronico.service;

import java.util.List;

import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.entity.Inconsistencia;
import com.br.ponto_eletronico.repository.InconsistenciaRepository;

public class InconsistenciaService {

    private InconsistenciaRepository repository =
            new InconsistenciaRepository();

    public List<Inconsistencia> listarPorFuncionario(Funcionario f) {
        return repository.buscarPorFuncionario(f.getId());
    }
}