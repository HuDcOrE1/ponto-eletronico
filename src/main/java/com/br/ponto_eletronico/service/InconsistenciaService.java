package com.br.ponto_eletronico.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.entity.Inconsistencia;
import com.br.ponto_eletronico.entity.TipoRegistro;
import com.br.ponto_eletronico.repository.InconsistenciaRepository;

public class InconsistenciaService {

    private InconsistenciaRepository repository =
            new InconsistenciaRepository();

    public List<Inconsistencia> listarPorFuncionario(Funcionario f) {
        return repository.buscarPorFuncionario(f.getId());
    }

    public List<Inconsistencia> getPorFuncionarioData(Funcionario f, LocalDateTime data) {
        return repository.getPorFuncionarioData(f.getId(), data);
    }

    public List<Inconsistencia> listarPorPeriodo(List<Inconsistencia> inconsistencias, YearMonth periodo) {
        return inconsistencias.stream().filter(i -> YearMonth.from(i.getHorario()).equals(periodo)).toList();
    }

    public List<Inconsistencia> listarTodas() {
        return repository.buscarTodas();
    }
}