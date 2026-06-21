package com.blog.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BlogScreenDTO {
    private Long authorId;
    private String author;
    private String authorProfilePictureUrl;
    private LocalDateTime localDateTime;
    private String title;
    private String subtitle;
    private String description;
}
