package com.blog.bo;

import com.blog.dao.BlogDAO;
import com.blog.dao.LikeDAO;
import com.blog.dao.UserDAO;
import com.blog.model.dto.LikeResponseDTO;
import com.blog.model.entity.Blog;
import com.blog.model.entity.Like;
import com.blog.model.entity.User;
import com.blog.validation.BusinessValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class LikeBO {
    @Inject
    LikeDAO dao;
    @Inject
    BlogDAO blogDAO;
    @Inject
    UserDAO userDAO;
    @Inject
    LikeDAO likeDAO;
    @Inject
    AuditLogBO auditLogBO;

    @Transactional
    public void create(Long userId, Long blogId) {
        BusinessValidator.validatePositiveId(userId, "Usuário");
        BusinessValidator.validatePositiveId(blogId, "Post");

        // 1. Busca as entidades reais gerenciadas pelo Hibernate
        User user = userDAO.findById(userId);
        Blog blog = blogDAO.findById(blogId);

        // 2. Valida a integridade (Evita NullPointerException e erros de FK no banco)
        if (user == null || blog == null) {
            throw new WebApplicationException("Usuário ou Blog não encontrado", Response.Status.NOT_FOUND);
        }

        long existingLike = likeDAO.count("user.id = ?1 and blog.id = ?2", userId, blogId);

        if (existingLike > 0) {
            return;
        }

        // 3. Monta a entidade Like
        Like like = new Like();
        like.setUser(user); // Passa a entidade completa e gerenciada
        like.setBlog(blog); // Passa a entidade completa e gerenciada

        // 4. Persiste no banco de dados
        dao.persist(like);
        auditLogBO.log("POST_LIKED", user, "Like no post " + blog.getId() + ".");
    }

    @Transactional
    public void remove(Long userId, Long blogId) {
        BusinessValidator.validatePositiveId(userId, "Usuário");
        BusinessValidator.validatePositiveId(blogId, "Post");

        User user = userDAO.findById(userId);
        long deletedCount = likeDAO.delete("user.id = ?1 and blog.id = ?2", userId, blogId);

        if (deletedCount > 0) {
            auditLogBO.log("POST_UNLIKED", user, "Like removido do post " + blogId + ".");
        }
    }

    public LikeResponseDTO list(Long blogId, Long currentUserId) {
        BusinessValidator.validatePositiveId(blogId, "Post");

        if (blogDAO.findById(blogId) == null) {
            throw new WebApplicationException("Post não encontrado", Response.Status.NOT_FOUND);
        }

        // 1. Conta o total de curtidas daquele post
        long total = likeDAO.count("blog.id", blogId);

        boolean hasLiked = false;

        // 2. Verifica se O usuário atual curtiu (só faz a query se houver um usuário logado)
        if (currentUserId != null) {
            // Retorna a contagem (0 ou 1) combinando o post e o usuário
            long userLikeCount = likeDAO.count("blog.id = ?1 and user.id = ?2", blogId, currentUserId);
            hasLiked = userLikeCount > 0;
        }

        // 3. Monta e devolve o DTO
        return  LikeResponseDTO.builder()
                .totalLikes(total)
                .userLiked(hasLiked)
                .build();
    }
}
