package com.br.ponto_eletronico.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.br.ponto_eletronico.config.JPAUtil;
import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.entity.Inconsistencia;
import jakarta.persistence.EntityManager;

public class InconsistenciaRepository {

    public void salvar(Inconsistencia inconsistencia) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(inconsistencia);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Inconsistencia> buscarPorFuncionario(Long funcionarioId) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                            "SELECT i FROM Inconsistencia i WHERE i.funcionario.id = :id ORDER BY i.horario",
                            Inconsistencia.class
                    )
                    .setParameter("id", funcionarioId)
                    .getResultList();

        } finally {
            em.close();
        }
    }


    public List<Inconsistencia> buscarTodas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT i FROM Inconsistencia i ORDER BY i.horario", Inconsistencia.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Inconsistencia> getPorFuncionarioData(Long funcionarioId,
                                                      LocalDateTime data) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            LocalDateTime inicio =
                    data.toLocalDate().atStartOfDay();
            LocalDateTime fim =
                    data.toLocalDate().plusDays(1)
                            .atStartOfDay()
                            .minusNanos(1);

            return em.createQuery(
                            """
                            SELECT i
                            FROM Inconsistencia i
                            WHERE i.funcionario.id = :id
                              AND i.horario BETWEEN :inicio AND :fim
                            ORDER BY i.horario
                            """,
                            Inconsistencia.class
                    )
                    .setParameter("id", funcionarioId)
                    .setParameter("inicio", inicio)
                    .setParameter("fim", fim)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public Inconsistencia buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Inconsistencia.class, id);
        } finally {
            em.close();
        }
    }

    public void deletar(Inconsistencia inconsistencia) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // re-attach local object to persistence context before removing
            Inconsistencia i = em.contains(inconsistencia) ? inconsistencia : em.merge(inconsistencia);
            em.remove(i);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Inconsistencia> buscarInconsistenciaPorData(Funcionario funcionario, LocalDate data) {
        EntityManager em = JPAUtil.getEntityManager();

        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(23, 59, 59);

        List<Inconsistencia> lista = em.createQuery(
                        "FROM Inconsistencia i WHERE i.funcionario = :funcionario " +
                                "AND i.horario BETWEEN :inicio AND :fim " +
                                "ORDER BY i.horario",
                        Inconsistencia.class)
                .setParameter("funcionario", funcionario)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();

        em.close();

        return lista;
    }

    public void deletarListaInconsistencia(List<Inconsistencia> inconsistencias) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            for (Inconsistencia i : inconsistencias) {
                em.getTransaction().begin();
                Inconsistencia inconsistenciaMerge = em.contains(i) ? i : em.merge(i);
                em.remove(inconsistenciaMerge);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}