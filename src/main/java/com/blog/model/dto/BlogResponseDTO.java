package com.blog.model.dto;

import com.blog.model.category.Category;
import com.blog.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class BlogResponseDTO {
    private Long id;
    private User user;
    private String title;
    private String subtitle;
    private String description;
    private Category category;
    private LocalDateTime localDateTime;
}
