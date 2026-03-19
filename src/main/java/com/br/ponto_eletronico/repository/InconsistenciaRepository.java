package com.br.ponto_eletronico.repository;

import java.util.List;

import com.br.ponto_eletronico.config.JPAUtil;
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
}