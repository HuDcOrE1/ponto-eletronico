package com.br.ponto_eletronico.repository;

import com.br.ponto_eletronico.config.JPAUtil;
import com.br.ponto_eletronico.entity.Funcionario;

import com.br.ponto_eletronico.entity.FuncionarioComum;
import com.br.ponto_eletronico.entity.Gestor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class FuncionarioRepository {

    public FuncionarioComum buscarPorMatricula(String matricula) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    "FROM Funcionario f WHERE f.matricula = :matricula",
                            FuncionarioComum.class)
                    .setParameter("matricula", matricula)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }

    }
    public Gestor buscarGestorComEquipe(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT g FROM Gestor g LEFT JOIN FETCH g.equipe WHERE g.id = :id",
                            Gestor.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();

        } finally {
            em.close();
        }
    }

    public Optional<Funcionario> login(String matricula) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            Funcionario funcionario = em.createQuery(
                            "FROM Funcionario f WHERE f.matricula = :matricula",
                            Funcionario.class
                    )
                    .setParameter("matricula", matricula)
                    .getSingleResult();

            return Optional.of(funcionario);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public void salvar(Funcionario funcionario) {

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(funcionario);
        em.getTransaction().commit();

        em.close();
    }

}