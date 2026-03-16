package com.br.ponto_eletronico.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("pontoPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}