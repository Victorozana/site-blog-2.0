package com.blog.model.dto;

import com.blog.model.Category;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BlogPrincipalDTO {
    private String title;
    private String subtitle;
    private String description;
    private LocalDateTime localDateTime;
}
