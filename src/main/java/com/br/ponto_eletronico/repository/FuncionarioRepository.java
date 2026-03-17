package com.br.ponto_eletronico.repository;

import com.br.ponto_eletronico.config.JPAUtil;
import com.br.ponto_eletronico.entity.Funcionario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class FuncionarioRepository {

    public Funcionario buscarPorMatricula(String matricula) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    "FROM Funcionario f WHERE f.matricula = :matricula",
                    Funcionario.class)
                    .setParameter("matricula", matricula)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
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