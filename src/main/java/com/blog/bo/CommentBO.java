package com.blog.bo;

import com.blog.dao.*;
import com.blog.model.entity.Comment;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class CommentBO implements ICommentBO{
    @Inject
    ICommentDAO commentDAO;
    @Inject
    IBlogDAO blogDAO;
    @Inject
    IUserDAO userDAO;

    @Override
    public void create(Long userId, Long blogId) {
        Comment entity = Comment.builder()
                .blog(blogDAO.findById(blogId))
                .user(userDAO.findById(userId))
                .build();

        commentDAO.persist(entity);
    }

    @Override
    public void remove(Long commentId) {
        commentDAO.delete(commentDAO.findById(commentId));
    }
}
