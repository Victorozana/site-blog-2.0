package com.blog.bo;


import com.blog.model.dto.BlogResponseDTO;
import com.blog.model.dto.BlogScreenDTO;
import com.blog.model.dto.MainScreenDTO;
import com.blog.model.dto.BlogRegistrationDTO;
import jakarta.ws.rs.core.Response;

import java.util.List;

public interface IBlogBO{
    BlogResponseDTO createBlog(BlogRegistrationDTO dto, Long idAuthor);

    void deleteBlog(Long id);

    BlogScreenDTO findBlogById(Long id);

    List<MainScreenDTO> blogList(int page, int size);
}
