package com.br.ponto_eletronico.service;

import java.time.YearMonth;
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

    public List<Inconsistencia> listarPorPeriodo(List<Inconsistencia> inconsistencias, YearMonth periodo) {
        return inconsistencias.stream().filter(i -> YearMonth.from(i.getHorario()).equals(periodo)).toList();
    }
}