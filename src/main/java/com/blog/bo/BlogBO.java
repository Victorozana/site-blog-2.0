package com.blog.bo;

import com.blog.dao.BlogDAO;
import com.blog.dao.CommentDAO;
import com.blog.dao.LikeDAO;
import com.blog.model.dto.BlogResponseDTO;
import com.blog.model.dto.BlogScreenDTO;
import com.blog.model.dto.MainScreenDTO;
import com.blog.model.dto.BlogRegistrationDTO;
import com.blog.model.entity.Blog;
import com.blog.model.entity.User;
import com.blog.validation.BusinessValidator;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BlogBO {
    @Inject
    BlogDAO blogDAO;
    @Inject
    CommentDAO commentDAO;
    @Inject
    LikeDAO likeDAO;
    @Inject
    UserBO userBO;
    @Inject
    AuditLogBO auditLogBO;

    @Transactional
    public BlogResponseDTO createBlog(BlogRegistrationDTO dto, Long idAuthor) {
        BusinessValidator.validatePositiveId(idAuthor, "Autor");
        BusinessValidator.validateBlogRegistration(dto);

        User user = userBO.getUserById(idAuthor);

        if (user == null) {
            throw new WebApplicationException("Autor não encontrado", Response.Status.NOT_FOUND);
        }

        Blog blog = new Blog();

        blog.setUser(user);
        blog.setCategory(dto.getCategory());
        blog.setSubtitle(dto.getSubtitle() == null ? null : dto.getSubtitle().trim());
        blog.setDescription(dto.getDescription().trim());
        blog.setTitle(dto.getTitle().trim());

        blogDAO.persist(blog);
        auditLogBO.log("BLOG_CREATED", user, "Post criado: " + blog.getTitle());

        return new BlogResponseDTO(
                blog.getId(),
                user.getId(),
                authorName(user),
                blog.getTitle(),
                blog.getSubtitle(),
                blog.getDescription(),
                blog.getCategory(),
                blog.getLocalDateTime()
        );
    }

    @Transactional
    public BlogResponseDTO updateBlog(Long blogId, Long userId, boolean admin, BlogRegistrationDTO dto) {
        BusinessValidator.validatePositiveId(blogId, "Post");
        BusinessValidator.validatePositiveId(userId, "Usuário");
        BusinessValidator.validateBlogRegistration(dto);

        Blog blog = blogDAO.findById(blogId);

        if (blog == null) {
            throw new WebApplicationException("Post não encontrado", Response.Status.NOT_FOUND);
        }

        if (!admin && !blog.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Você não tem permissão para editar este post.", Response.Status.FORBIDDEN);
        }

        blog.setTitle(dto.getTitle().trim());
        blog.setSubtitle(dto.getSubtitle() == null ? null : dto.getSubtitle().trim());
        blog.setDescription(dto.getDescription().trim());
        blog.setCategory(dto.getCategory());

        blogDAO.persist(blog);
        auditLogBO.log("BLOG_UPDATED", blog.getUser(), "Post atualizado: " + blog.getTitle());

        return toBlogResponseDTO(blog);
    }

    @Transactional
    public void deleteBlog(Long id, Long userId, boolean admin) {
        BusinessValidator.validatePositiveId(id, "Post");
        BusinessValidator.validatePositiveId(userId, "Usuário");
        Blog blog = blogDAO.findById(id);

        if (blog != null) {
            if (!admin && !blog.getUser().getId().equals(userId)) {
                throw new WebApplicationException("Você não tem permissão para excluir este post.", Response.Status.FORBIDDEN);
            }

            auditLogBO.log("BLOG_DELETED", blog.getUser(), "Post removido: " + blog.getTitle());
            likeDAO.delete("blog.id", id);
            commentDAO.delete("blog.id", id);
            blogDAO.delete(blog);
        }
    }

    public BlogScreenDTO findBlogById(Long id) {
        BusinessValidator.validatePositiveId(id, "Post");
        Blog blog = blogDAO.findById(id);

        if (blog == null) {
            throw new WebApplicationException("Post não encontrado", Response.Status.NOT_FOUND);
        }

        BlogScreenDTO dto = new BlogScreenDTO();
        dto.setId(blog.getId());
        dto.setAuthorId(blog.getUser().getId());
        dto.setAuthor(authorName(blog.getUser()));
        dto.setAuthorProfilePictureUrl(blog.getUser().getProfilePictureUrl());
        dto.setDescription(blog.getDescription());
        dto.setTitle(blog.getTitle());
        dto.setSubtitle(blog.getSubtitle());
        dto.setCategory(blog.getCategory());
        dto.setLocalDateTime(blog.getLocalDateTime());

        return dto;
    }

    public List<MainScreenDTO> blogList(int page, int size) {
        if (page < 0) {
            throw new WebApplicationException("Página inválida", Response.Status.BAD_REQUEST);
        }

        if (size < 1 || size > 50) {
            throw new WebApplicationException("Tamanho da página inválido", Response.Status.BAD_REQUEST);
        }

        List<Blog> blogs = blogDAO.find("order by localDateTime desc").page(Page.of(page, size)).list();
        List<MainScreenDTO> dtos = new ArrayList<>();

        for (Blog blog : blogs){
            MainScreenDTO dto = new MainScreenDTO();
            dto.setAuthorId(blog.getUser().getId());
            dto.setAuthor(authorName(blog.getUser()));
            dto.setAuthorProfilePictureUrl(blog.getUser().getProfilePictureUrl());
            dto.setId(blog.getId());
            dto.setTitle(blog.getTitle());
            dto.setSubtitle(blog.getSubtitle());
            dto.setDescription(blog.getDescription());
            dto.setLocalDateTime(blog.getLocalDateTime());

            dtos.add(dto);
        }

        return dtos;
    }

    public List<BlogResponseDTO> manageableBlogList(Long userId, boolean admin) {
        BusinessValidator.validatePositiveId(userId, "Usuário");

        List<Blog> blogs = admin
                ? blogDAO.find("order by localDateTime desc").list()
                : blogDAO.find("user.id = ?1 order by localDateTime desc", userId).list();

        return blogs.stream()
                .map(this::toBlogResponseDTO)
                .toList();
    }

    private String authorName(User user) {
        if (user.getLastname() == null || user.getLastname().isBlank()) {
            return user.getName();
        }

        return user.getName() + " " + user.getLastname();
    }

    private BlogResponseDTO toBlogResponseDTO(Blog blog) {
        return new BlogResponseDTO(
                blog.getId(),
                blog.getUser().getId(),
                authorName(blog.getUser()),
                blog.getTitle(),
                blog.getSubtitle(),
                blog.getDescription(),
                blog.getCategory(),
                blog.getLocalDateTime()
        );
    }
}
