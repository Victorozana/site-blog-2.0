package com.blog.model.dto;

import com.blog.model.category.Category;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class
BlogRegistrationDTO {
    private Long userId;
    private String title;
    private String subtitle;
    private String description;
    private Category category;
}
