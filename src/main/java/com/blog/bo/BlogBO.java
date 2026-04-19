package com.blog.bo;

import com.blog.dao.IBlogDAO;
import com.blog.model.dto.BlogRegistrationDTO;
import com.blog.model.dto.BlogSummaryDTO;
import com.blog.model.entity.Blog;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class BlogBO implements IBlogBO{
    @Inject
    IBlogDAO blogDAO;

    @Override
    public Response createBlog(BlogRegistrationDTO dto) {
        Blog blog = new Blog();

        blog.setCategory(dto.getCategory());
        blog.setSubtitle(dto.getTitle());
        blog.setDescription(dto.getDescription());

        blogDAO.persist(blog);
        return Response.status(Response.Status.CREATED).entity(blog).build();
    }

    @Override
    public Response deleteBlog(Long id) {
        blogDAO.deleteById(id);
        return Response.status(Response.Status.OK).entity("Deletado com sucesso").build();    }

    @Override
    public List<BlogSummaryDTO> blogList() {
        List<Blog> blogs = blogDAO.listAll();
        List<BlogSummaryDTO> dtos = new ArrayList<>();
        for (Blog blog : blogs) {
                for (BlogSummaryDTO dto : dtos){
                    dto.setCategory(blog.getCategory());
                    dto.setSubtitle(blog.getSubtitle());
                    dto.setTitle(blog.getTitle());
                    dto.setUser(blog.getUser());
                }
        }
        return dtos;
    }
}
