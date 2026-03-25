package com.br.ponto_eletronico.repository;

import com.br.ponto_eletronico.config.JPAUtil;
import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.entity.RegistroPonto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RelatorioRepository {

    public List<Funcionario> buscarTodosFuncionarios() {
        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                            "SELECT f FROM Funcionario f",
                            Funcionario.class)
                    .getResultList();

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<RegistroPonto> buscarPorFuncionario(Long funcionarioId, int ano, int mes) {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDateTime inicio = LocalDate.of(ano, mes, 1).atStartOfDay();
            LocalDateTime fim = inicio.plusMonths(1);

            return em.createQuery(
                            "SELECT rp FROM RegistroPonto rp " +
                                    "WHERE rp.funcionario.id = :funcionarioId " +
                                    "AND rp.horario >= :inicio " +
                                    "AND rp.horario < :fim " +
                                    "ORDER BY rp.horario",
                            RegistroPonto.class)
                    .setParameter("funcionarioId", funcionarioId)
                    .setParameter("inicio", inicio)
                    .setParameter("fim", fim)
                    .getResultList();

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<RegistroPonto> buscarPorFuncionario(Funcionario funcionario, int ano, int mes, int dia) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            LocalDateTime inicio = LocalDateTime.of(ano, mes, dia, 0, 0);
            LocalDateTime fim = inicio.plusDays(1);

            return em.createQuery(
                            "SELECT rp FROM RegistroPonto rp " +
                                    "JOIN rp.funcionario f " +
                                    "WHERE f.matricula = :matricula " +
                                    "AND rp.horario >= :inicio " +
                                    "AND rp.horario < :fim " +
                                    "ORDER BY rp.horario",
                            RegistroPonto.class)
                    .setParameter("matricula", funcionario.getMatricula())
                    .setParameter("inicio", inicio)
                    .setParameter("fim", fim)
                    .getResultList();

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public long contarInconsistencias(Long funcionarioId, int ano, int mes) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            LocalDateTime inicio = LocalDate.of(ano, mes, 1).atStartOfDay();
            LocalDateTime fim = inicio.plusMonths(1);

            return em.createQuery(
                            "SELECT COUNT(i) FROM Inconsistencia i " +
                                    "WHERE i.funcionario.id = :funcionarioId " +
                                    "AND i.horario >= :inicio " +
                                    "AND i.horario < :fim",
                            Long.class)
                    .setParameter("funcionarioId", funcionarioId)
                    .setParameter("inicio", inicio)
                    .setParameter("fim", fim)
                    .getSingleResult();

        } finally {
            em.close();
        }
    }

}