package com.blog.bo;

import com.blog.dao.BlogDAO;
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
    public void deleteBlog(Long id) {
        BusinessValidator.validatePositiveId(id, "Post");
        Blog blog = blogDAO.findById(id);

        if (blog != null) {
            auditLogBO.log("BLOG_DELETED", blog.getUser(), "Post removido: " + blog.getTitle());
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
        dto.setAuthorId(blog.getUser().getId());
        dto.setAuthor(authorName(blog.getUser()));
        dto.setAuthorProfilePictureUrl(blog.getUser().getProfilePictureUrl());
        dto.setDescription(blog.getDescription());
        dto.setTitle(blog.getTitle());
        dto.setSubtitle(blog.getSubtitle());
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

    private String authorName(User user) {
        if (user.getLastname() == null || user.getLastname().isBlank()) {
            return user.getName();
        }

        return user.getName() + " " + user.getLastname();
    }
}
