package com.br.ponto_eletronico.repository;

import com.br.ponto_eletronico.config.JPAUtil;
import com.br.ponto_eletronico.entity.Funcionario;
import com.br.ponto_eletronico.entity.RegistroPonto;

import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RegistroPontoRepository {

    public void salvar(RegistroPonto registro) {

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(registro);
        em.getTransaction().commit();

        em.close();
    }

    public List<RegistroPonto> buscarRegistrosHoje(Funcionario funcionario) {

        EntityManager em = JPAUtil.getEntityManager();

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicio = hoje.atStartOfDay();
        LocalDateTime fim = hoje.atTime(23,59,59);

        List<RegistroPonto> lista = em.createQuery(
                "FROM RegistroPonto r WHERE r.funcionario = :funcionario " +
                "AND r.horario BETWEEN :inicio AND :fim " +
                "ORDER BY r.horario",
                RegistroPonto.class)
                .setParameter("funcionario", funcionario)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();

        em.close();

        return lista;
    }

    public List<RegistroPonto> buscarRegistrosDia(Funcionario funcionario, LocalDate dia) {

        EntityManager em = JPAUtil.getEntityManager();

        LocalDateTime inicio = dia.atStartOfDay();
        LocalDateTime fim = dia.atTime(23,59,59);

        List<RegistroPonto> lista = em.createQuery(
                        "FROM RegistroPonto r WHERE r.funcionario = :funcionario " +
                                "AND r.horario BETWEEN :inicio AND :fim " +
                                "ORDER BY r.horario",
                        RegistroPonto.class)
                .setParameter("funcionario", funcionario)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();

        em.close();

        return lista;
    }

    public List<RegistroPonto> buscarRegistrosPorFuncionario(Funcionario funcionario){
        EntityManager em = JPAUtil.getEntityManager();

        List<RegistroPonto> lista = em.createQuery(
                "FROM RegistroPonto r WHERE r.funcionario = :funcionario " +
                "ORDER BY r.horario",
                RegistroPonto.class)
                .setParameter("funcionario", funcionario).getResultList();

        return lista;
    }

    
    public List<RegistroPonto> buscarRegistrosPorData(Funcionario funcionario, LocalDate data) {
        EntityManager em = JPAUtil.getEntityManager();

        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(23, 59, 59);

        List<RegistroPonto> lista = em.createQuery(
                        "FROM RegistroPonto r WHERE r.funcionario = :funcionario " +
                                "AND r.horario BETWEEN :inicio AND :fim " +
                                "ORDER BY r.horario",
                        RegistroPonto.class)
                .setParameter("funcionario", funcionario)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();

        em.close();

        return lista;
    }

    public void deletarLista(List<RegistroPonto> registros) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            for (RegistroPonto r : registros) {
                RegistroPonto registroMerged = em.contains(r) ? r : em.merge(r);
                em.remove(registroMerged);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}