package com.blog.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MainScreenDTO {
    private Long id;
    private Long authorId;
    private String author;
    private String authorProfilePictureUrl;
    private String title;
    private String subtitle;
    private String description;
    private LocalDateTime localDateTime;
}
