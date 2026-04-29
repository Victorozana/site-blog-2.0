package com.blog.model.dto;

import com.blog.model.Category;
import com.blog.model.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogSummaryDTO {
    private User user;
    private String title;
    private String subtitle;
    private Category category;
}
