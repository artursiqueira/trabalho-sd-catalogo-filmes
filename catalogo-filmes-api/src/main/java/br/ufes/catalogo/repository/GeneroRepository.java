package br.ufes.catalogo.repository;

import br.ufes.catalogo.config.JPAUtil;
import br.ufes.catalogo.model.Genero;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class GeneroRepository {

    public Genero salvar(Genero genero) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (genero.getId() == null) {
                em.persist(genero);
            } else {
                genero = em.merge(genero);
            }
            em.getTransaction().commit();
            return genero;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar gênero", e);
        } finally {
            em.close();
        }
    }

    public Optional<Genero> buscarPorId(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Genero genero = em.find(Genero.class, id);
            return Optional.ofNullable(genero);
        }
    }

    public List<Genero> listarTodos() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT g FROM Genero g ORDER BY g.nome", Genero.class)
                    .getResultList();
        }
    }

    public void deletar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Genero genero = em.find(Genero.class, id);
            if (genero != null) {
                em.remove(genero);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao deletar gênero", e);
        } finally {
            em.close();
        }
    }
}
