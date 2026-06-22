package com.blog.model.dto;

import com.blog.model.category.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BlogScreenDTO {
    private Long id;
    private Long authorId;
    private String author;
    private String authorProfilePictureUrl;
    private LocalDateTime localDateTime;
    private String title;
    private String subtitle;
    private String description;
    private Category category;
}
