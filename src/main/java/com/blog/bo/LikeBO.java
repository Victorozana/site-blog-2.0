package com.blog.bo;

import com.blog.dao.BlogDAO;
import com.blog.dao.ILikeDAO;
import com.blog.dao.UserDAO;
import com.blog.model.entity.Like;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class LikeBO implements ILikeBO{
    @Inject
    ILikeDAO dao;
    @Inject
    BlogDAO blogDAO;
    @Inject
    UserDAO userDAO;

    @Override
    public void create(Long userId, Long blogId) {
        Like entity = Like.builder()
                    .blog(blogDAO.findById(blogId))
                    .user(userDAO.findById(userId))
                    .build();

        dao.persist(entity);
    }

    @Override
    public void remove(Long idLike) {
        dao.delete(dao.findById(idLike));
    }
}
