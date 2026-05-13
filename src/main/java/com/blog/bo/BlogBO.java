package com.blog.bo;

import com.blog.dao.IBlogDAO;
import com.blog.model.dto.BlogPrincipalDTO;
import com.blog.model.dto.BlogRegistrationDTO;
import com.blog.model.entity.Blog;
import com.blog.model.entity.User;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class BlogBO implements IBlogBO{
    @Inject
    IBlogDAO blogDAO;
    @Inject
    UserBO userBO;

    @Override
    @Transactional
    public Response createBlog(BlogRegistrationDTO dto) {
        Blog blog = new Blog();
        User user = new User();
        user = userBO.getUserById(dto.getUserId());

        blog.setUser(user);
        blog.setCategory(dto.getCategory());
        blog.setSubtitle(dto.getSubtitle());
        blog.setDescription(dto.getDescription());
        blog.setTitle(dto.getTitle());

        System.out.println("description");

        blogDAO.persist(blog);
    return Response.status(Response.Status.CREATED).entity(blog).build();
    }

    @Override
    @Transactional
    public Response deleteBlog(Long id) {
        blogDAO.deleteById(id);
        return Response.status(Response.Status.OK).entity("Deletado com sucesso").build();    }


    @Override
    public BlogPrincipalDTO findBlogById(Long id) {
        Blog blog = new Blog();
        blog = blogDAO.findById(id);
        BlogPrincipalDTO dto = new BlogPrincipalDTO();

        dto.setDescription(blog.getDescription());
        dto.setLocalDateTime(blog.getLocalDateTime());
        dto.setTitle(blog.getTitle());
        dto.setSubtitle(blog.getSubtitle());

        return dto;
    }

    @Override
    public List<BlogPrincipalDTO> blogList(int page, int size) {
        List<Blog> blogs = blogDAO.find("order by localDateTime desc").page(Page.of(page, size)).list();
        List<BlogPrincipalDTO> dtos = new ArrayList<>();

        for (Blog blog : blogs){
            BlogPrincipalDTO dto = new BlogPrincipalDTO();
            dto.setTitle(blog.getTitle());
            dto.setSubtitle(blog.getSubtitle());
            dto.setDescription(blog.getDescription());
            dto.setLocalDateTime(blog.getLocalDateTime());

            dtos.add(dto);
        }

        return dtos;
    }
}