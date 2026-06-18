package com.blog.bo;

import com.blog.dao.BlogDAO;
import com.blog.dao.ILikeDAO;
import com.blog.dao.UserDAO;
import com.blog.model.entity.Blog;
import com.blog.model.entity.Like;
import com.blog.model.entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@RequestScoped
public class LikeBO implements ILikeBO{
    @Inject
    ILikeDAO dao;
    @Inject
    BlogDAO blogDAO;
    @Inject
    UserDAO userDAO;

    @Override
    @Transactional
    public void create(Long userId, Long blogId) {
// 1. Busca as entidades reais gerenciadas pelo Hibernate
        User user = userDAO.findById(userId);
        Blog blog = blogDAO.findById(blogId);

        // 2. Valida a integridade (Evita NullPointerException e erros de FK no banco)
        if (user == null || blog == null) {
            throw new WebApplicationException("Usuário ou Blog não encontrado", Response.Status.NOT_FOUND);
        }

        // 3. Monta a entidade Like
        Like like = new Like();
        like.setUser(user); // Passa a entidade completa e gerenciada
        like.setBlog(blog); // Passa a entidade completa e gerenciada

        // 4. Persiste no banco de dados
        dao.persist(like);
    }

    @Override
    public void remove(Long idLike) {
        dao.delete(dao.findById(idLike));
    }
}
