package com.blog.bo;

import com.blog.dao.*;
import com.blog.model.dto.CommentRequestDTO;
import com.blog.model.entity.Blog;
import com.blog.model.entity.Comment;
import com.blog.model.entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@RequestScoped
public class CommentBO implements ICommentBO{
    @Inject
    ICommentDAO commentDAO;
    @Inject
    IBlogDAO blogDAO;
    @Inject
    IUserDAO userDAO;

    @Override
    @Transactional
    public void create(Long userId, Long blogId, CommentRequestDTO dto) {
        Blog blog = blogDAO.findById(blogId);
        User user = userDAO.findById(userId);

        if (user == null || blog == null) {
            throw new WebApplicationException("Usuário ou Blog não encontrado", Response.Status.NOT_FOUND);
        }

        Comment entity = Comment.builder()
                .user(user)
                .blog(blog)
                .comment(dto.getComment())
                .build();

        commentDAO.persist(entity);
    }

    @Override
    public void remove(Long commentId) {
        commentDAO.delete(commentDAO.findById(commentId));
    }
}
