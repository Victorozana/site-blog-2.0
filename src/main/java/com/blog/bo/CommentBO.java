package com.blog.bo;

import com.blog.dao.*;
import com.blog.model.dto.CommentRequestDTO;
import com.blog.model.dto.CommentResponseDTO;
import com.blog.model.entity.Blog;
import com.blog.model.entity.Comment;
import com.blog.model.entity.User;
import com.blog.validation.BusinessValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CommentBO {
    @Inject
    CommentDAO commentDAO;
    @Inject
    BlogDAO blogDAO;
    @Inject
    UserDAO userDAO;
    @Inject
    AuditLogBO auditLogBO;

    @Transactional
    public void create(Long userId, Long blogId, CommentRequestDTO dto) {
        BusinessValidator.validatePositiveId(userId, "Usuário");
        BusinessValidator.validatePositiveId(blogId, "Post");
        BusinessValidator.validateComment(dto != null ? dto.getComment() : null);

        Blog blog = blogDAO.findById(blogId);
        User user = userDAO.findById(userId);

        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.UNAUTHORIZED);
        }

        if (blog == null) {
            throw new WebApplicationException("Blog não encontrado", Response.Status.NOT_FOUND);
        }

        Comment entity = Comment.builder()
                .user(user)
                .blog(blog)
                .comment(dto.getComment().trim())
                .build();

        commentDAO.persist(entity);
        auditLogBO.log("COMMENT_CREATED", user, "Comentário criado no post " + blog.getId() + ".");
    }

    public void remove(Long commentId) {
        BusinessValidator.validatePositiveId(commentId, "Comentário");
        Comment comment = commentDAO.findById(commentId);

        if (comment != null) {
            auditLogBO.log("COMMENT_DELETED", comment.getUser(), "Comentário removido do post " + comment.getBlog().getId() + ".");
            commentDAO.delete(comment);
        }
    }

    public List<CommentResponseDTO> list(Long blogId) {
        BusinessValidator.validatePositiveId(blogId, "Post");

        if (blogDAO.findById(blogId) == null) {
            throw new WebApplicationException("Blog não encontrado", Response.Status.NOT_FOUND);
        }

        return commentDAO.find("blog.id = ?1 and user is not null order by id asc", blogId).stream()
                .map(comment -> CommentResponseDTO.builder()
                        .id(comment.getId())
                        .comment(comment.getComment())
                        .author(authorName(comment.getUser()))
                        .authorProfilePictureUrl(comment.getUser().getProfilePictureUrl())
                        .build())
                .collect(Collectors.toList());
    }

    private String authorName(User user) {
        if (user.getLastname() == null || user.getLastname().isBlank()) {
            return user.getName();
        }

        return user.getName() + " " + user.getLastname();
    }
}
