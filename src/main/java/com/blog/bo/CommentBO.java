package com.blog.bo;

import com.blog.dao.*;
import com.blog.model.dto.CommentRequestDTO;
import com.blog.model.dto.CommentResponseDTO;
import com.blog.model.entity.Blog;
import com.blog.model.entity.Comment;
import com.blog.model.entity.User;
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
        Blog blog = blogDAO.findById(blogId);
        User user = userDAO.findById(userId);

        if (user == null || blog == null) {
            throw new WebApplicationException("Usuário ou Blog não encontrado", Response.Status.NOT_FOUND);
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
        Comment comment = commentDAO.findById(commentId);

        if (comment != null) {
            auditLogBO.log("COMMENT_DELETED", comment.getUser(), "Comentário removido do post " + comment.getBlog().getId() + ".");
            commentDAO.delete(comment);
        }
    }

    public List<CommentResponseDTO> list(Long blogId) {
        return commentDAO.find("blog.id", blogId).stream()
                .map(comment -> {
                    return CommentResponseDTO.builder()
                            .id(comment.getId())
                            .comment(comment.getComment()) // ou getComment(), dependendo de como está na sua entidade
                            .author(comment.getUser() != null ? authorName(comment.getUser()) : "Anônimo")
                            .authorProfilePictureUrl(comment.getUser() != null ? comment.getUser().getProfilePictureUrl() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String authorName(User user) {
        if (user.getLastname() == null || user.getLastname().isBlank()) {
            return user.getName();
        }

        return user.getName() + " " + user.getLastname();
    }
}
