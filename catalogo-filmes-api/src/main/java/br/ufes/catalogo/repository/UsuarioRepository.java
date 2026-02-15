package br.ufes.catalogo.repository;

import br.ufes.catalogo.config.JPAUtil;
import br.ufes.catalogo.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;

public class UsuarioRepository {

    public Usuario salvar(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (usuario.getId() == null) {
                em.persist(usuario);
            } else {
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
            return usuario;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar usuário", e);
        } finally {
            em.close();
        }
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Usuario usuario = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.username = :username",
                            Usuario.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Usuario usuario = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.email = :email",
                            Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Usuario> buscarPorId(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Usuario usuario = em.find(Usuario.class, id);
            return Optional.ofNullable(usuario);
        }
    }
}
