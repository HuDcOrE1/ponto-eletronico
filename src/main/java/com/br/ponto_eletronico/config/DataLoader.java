package com.br.ponto_eletronico.config;

import com.br.ponto_eletronico.entity.Funcionario;
import jakarta.persistence.EntityManager;

import java.util.Random;

public class DataLoader {

    public static void load() {
        EntityManager em = JPAUtil.getEntityManager();
        Random random = new Random();

        String[] nomes = {"João", "Maria", "Carlos", "Ana", "Pedro", "Lucas", "Marcos", "Juliana", "Hudson", "Jaime", "Jean", "Adriano", "Gustavo"};
        String[] sobrenomes = {"Silva", "Souza", "Oliveira", "Santos", "Pereira", "Costa", "Rodrigues", "Moraes", "Bahia", "Fernandes"};

        try {
            Long count = em.createQuery(
                    "SELECT COUNT(f) FROM Funcionario f",
                    Long.class
            ).getSingleResult();

            if (count == 0) {
                em.getTransaction().begin();

                for (int i = 1; i <= 20; i++) {
                    Funcionario f = new Funcionario();

                    f.setMatricula(String.valueOf(1001 + i));
                    f.setSenha("123");

                    String nome = nomes[random.nextInt(nomes.length)];
                    String sobrenome = sobrenomes[random.nextInt(sobrenomes.length)];
                    f.setNome(nome + " " + sobrenome);

                    f.setGestor(random.nextBoolean());

                    em.persist(f);
                }

                em.getTransaction().commit();

                System.out.println("Usuários criados");
            }
        } finally {
            em.close();
        }
    }
}