package com.br.ponto_eletronico.config;

import com.br.ponto_eletronico.entity.Funcionario;
import jakarta.persistence.EntityManager;

public class DataLoader {

    public static void load() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            Long count = em.createQuery(
                "SELECT COUNT(f) FROM Funcionario f",
                Long.class
            ).getSingleResult();

            if (count == 0) {

                em.getTransaction().begin();

                Funcionario f = new Funcionario();
                f.setMatricula("1001");
                f.setSenha("123");
                f.setNome("Admin");

                em.persist(f);

                em.getTransaction().commit();

                System.out.println("Usuário inicial criado");

            }

        } finally {
            em.close();
        }
    }
}