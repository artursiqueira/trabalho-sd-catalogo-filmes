package br.ufes.catalogo.repository;

import br.ufes.catalogo.config.JPAUtil;
import br.ufes.catalogo.model.Filme;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class FilmeRepository {

    public Filme salvar(Filme filme) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (filme.getId() == null) {
                em.persist(filme);
            } else {
                filme = em.merge(filme);
            }
            em.getTransaction().commit();
            return filme;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar filme", e);
        } finally {
            em.close();
        }
    }

    public Optional<Filme> buscarPorId(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Filme filme = em.find(Filme.class, id);
            return Optional.ofNullable(filme);
        }
    }

    public List<Filme> listarTodos() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT f FROM Filme f", Filme.class)
                    .getResultList();
        }
    }

    public List<Filme> listarPaginado(int pagina, int tamanho) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT f FROM Filme f ORDER BY f.id", Filme.class)
                    .setFirstResult(pagina * tamanho)
                    .setMaxResults(tamanho)
                    .getResultList();
        }
    }

    public long contarTotal() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT COUNT(f) FROM Filme f", Long.class)
                    .getSingleResult();
        }
    }

    public List<Filme> buscarPorTitulo(String titulo) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT f FROM Filme f WHERE LOWER(f.titulo) LIKE LOWER(:titulo)",
                            Filme.class)
                    .setParameter("titulo", "%" + titulo + "%")
                    .getResultList();
        }
    }

    public List<Filme> buscarPorGenero(Long generoId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT f FROM Filme f WHERE f.genero.id = :generoId",
                            Filme.class)
                    .setParameter("generoId", generoId)
                    .getResultList();
        }
    }

    public List<Filme> buscarPorDiretor(String diretor) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT f FROM Filme f WHERE LOWER(f.diretor) LIKE LOWER(:diretor)",
                            Filme.class)
                    .setParameter("diretor", "%" + diretor + "%")
                    .getResultList();
        }
    }

    public void deletar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Filme filme = em.find(Filme.class, id);
            if (filme != null) {
                em.remove(filme);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao deletar filme", e);
        } finally {
            em.close();
        }
    }

    public Optional<Filme> buscarPorIdComAtores(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Filme> result = em.createQuery(
                            "SELECT DISTINCT f FROM Filme f " +
                                    "LEFT JOIN FETCH f.atores " +
                                    "LEFT JOIN FETCH f.genero " +
                                    "WHERE f.id = :id",
                            Filme.class)
                    .setParameter("id", id)
                    .getResultList();
            return result.stream().findFirst();
        }
    }

    public List<Filme> listarTodosComAtores() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT f FROM Filme f " +
                                    "LEFT JOIN FETCH f.atores " +
                                    "LEFT JOIN FETCH f.genero",
                            Filme.class)
                    .getResultList();
        }
    }

    public List<Filme> listarPaginadoComAtores(int pagina, int tamanho) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Long> ids = em.createQuery(
                            "SELECT f.id FROM Filme f ORDER BY f.id",
                            Long.class)
                    .setFirstResult(pagina * tamanho)
                    .setMaxResults(tamanho)
                    .getResultList();

            if (ids.isEmpty()) {
                return List.of();
            }

            return em.createQuery(
                            "SELECT DISTINCT f FROM Filme f " +
                                    "LEFT JOIN FETCH f.atores " +
                                    "LEFT JOIN FETCH f.genero " +
                                    "WHERE f.id IN :ids " +
                                    "ORDER BY f.id",
                            Filme.class)
                    .setParameter("ids", ids)
                    .getResultList();
        }
    }

    public List<Filme> buscarPorTituloComAtores(String titulo) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT f FROM Filme f " +
                                    "LEFT JOIN FETCH f.atores " +
                                    "LEFT JOIN FETCH f.genero " +
                                    "WHERE LOWER(f.titulo) LIKE LOWER(:titulo)",
                            Filme.class)
                    .setParameter("titulo", "%" + titulo + "%")
                    .getResultList();
        }
    }

    public List<Filme> buscarPorGeneroComAtores(Long generoId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT f FROM Filme f " +
                                    "LEFT JOIN FETCH f.atores " +
                                    "LEFT JOIN FETCH f.genero " +
                                    "WHERE f.genero.id = :generoId",
                            Filme.class)
                    .setParameter("generoId", generoId)
                    .getResultList();
        }
    }

    public List<Filme> buscarPorDiretorComAtores(String diretor) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT f FROM Filme f " +
                                    "LEFT JOIN FETCH f.atores " +
                                    "LEFT JOIN FETCH f.genero " +
                                    "WHERE LOWER(f.diretor) LIKE LOWER(:diretor)",
                            Filme.class)
                    .setParameter("diretor", "%" + diretor + "%")
                    .getResultList();
        }
    }
}

