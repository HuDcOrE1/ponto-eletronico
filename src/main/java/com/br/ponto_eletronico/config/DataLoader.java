package com.br.ponto_eletronico.config;

import com.br.ponto_eletronico.console.GeradorPontos;
import com.br.ponto_eletronico.entity.*;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataLoader {

    public static void load() {
        EntityManager em = JPAUtil.getEntityManager();
        Random random = new Random();

        String[] nomes = {"João", "Maria", "Carlos", "Ana", "Pedro", "Lucas",
                "Marcos", "Juliana", "Hudson", "Jaime", "Jean",
                "Adriano", "Gustavo"};
        String[] sobrenomes = {"Silva", "Souza", "Oliveira", "Santos",
                "Pereira", "Costa", "Rodrigues",
                "Moraes", "Bahia", "Fernandes"};

        try {
            em.getTransaction().begin();

            limparBanco(em);

            List<Gestor> gestores = new ArrayList<>();

            // ============================
            // 1️⃣ CRIAR GESTORES
            // ============================
            for (int i = 1; i <= 5; i++) {
                Gestor g = new Gestor();
                g.setMatricula(String.valueOf(2000 + i));
                g.setSenha("123");
                g.setNome(
                        nomes[random.nextInt(nomes.length)] + " " +
                                sobrenomes[random.nextInt(sobrenomes.length)]
                );

                em.persist(g);
                gestores.add(g);
            }

            // ============================
            // 2️⃣ CRIAR FUNCIONÁRIOS COMUNS (JÁ COM GESTOR)
            // ============================
            for (int i = 1; i <= 15; i++) {
                Gestor gestorAleatorio =
                        gestores.get(random.nextInt(gestores.size()));

                FuncionarioComum f = new FuncionarioComum();
                f.setMatricula(String.valueOf(3000 + i));
                f.setSenha("123");
                f.setNome(
                        nomes[random.nextInt(nomes.length)] + " " +
                                sobrenomes[random.nextInt(sobrenomes.length)]
                );
                f.setGestor(gestorAleatorio);

                gestorAleatorio.getEquipe().add(f);

                em.persist(f); // ✅ agora NÃO é nulo
            }

            em.getTransaction().commit();
            System.out.println("✅ Banco recriado e populado com sucesso!");

            // ============================
            // 3 GERAR OS PONTOS
            // ============================
            GeradorPontos.executar();

        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static void limparBanco(EntityManager em) {
        em.createQuery("DELETE FROM RegistroPonto").executeUpdate();
        em.createQuery("DELETE FROM Inconsistencia").executeUpdate();
        em.createQuery("DELETE FROM FuncionarioComum").executeUpdate();
        em.createQuery("DELETE FROM Gestor").executeUpdate();
        em.createQuery("DELETE FROM Funcionario").executeUpdate();
    }
}