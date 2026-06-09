package com.blog.bo;

import com.blog.dao.IBlogDAO;
import com.blog.model.dto.BlogResponseDTO;
import com.blog.model.dto.BlogScreenDTO;
import com.blog.model.dto.MainScreenDTO;
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
    public BlogResponseDTO createBlog(BlogRegistrationDTO dto, Long idAuthor) {
        User user = userBO.getUserById(idAuthor);
        Blog blog = new Blog();

        blog.setUser(user);
        blog.setCategory(dto.getCategory());
        blog.setSubtitle(dto.getSubtitle());
        blog.setDescription(dto.getDescription());
        blog.setTitle(dto.getTitle());

        blogDAO.persist(blog);

        return new BlogResponseDTO(blog.getId(), blog.getUser(), blog.getTitle(), blog.getSubtitle(), blog.getDescription(), blog.getCategory(), blog.getLocalDateTime());
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) {
        blogDAO.deleteById(id);
    }


    @Override
    public BlogScreenDTO findBlogById(Long id) {
        Blog blog = blogDAO.findById(id);
        BlogScreenDTO dto = new BlogScreenDTO();

        System.out.println(blog.getTitle());

        dto.setAuthor(blog.getUser().getName());
        dto.setDescription(blog.getDescription());
        dto.setTitle(blog.getTitle());
        dto.setSubtitle(blog.getSubtitle());
        dto.setLocalDateTime(blog.getLocalDateTime());

        System.out.println(dto.getTitle());

        return dto;
    }

    @Override
    public List<MainScreenDTO> blogList(int page, int size) {
        List<Blog> blogs = blogDAO.find("order by localDateTime desc").page(Page.of(page, size)).list();
        List<MainScreenDTO> dtos = new ArrayList<>();

        for (Blog blog : blogs){
            MainScreenDTO dto = new MainScreenDTO();
            dto.setAuthor(blog.getUser().getName());
            dto.setId(blog.getId());
            dto.setTitle(blog.getTitle());
            dto.setSubtitle(blog.getSubtitle());
            dto.setDescription(blog.getDescription());
            dto.setLocalDateTime(blog.getLocalDateTime());

            dtos.add(dto);
        }

        return dtos;
    }
}