package com.blog.bo;


import com.blog.model.dto.BlogPrincipalDTO;
import com.blog.model.dto.BlogRegistrationDTO;
import com.blog.model.dto.BlogSummaryDTO;
import com.blog.model.entity.Blog;
import jakarta.ws.rs.core.Response;

import java.util.List;

public interface IBlogBO{
    Response createBlog(BlogRegistrationDTO dto);

    Response deleteBlog(Long id);

    List<Blog> blogList();

    BlogPrincipalDTO findBlogById(Long id);
}
