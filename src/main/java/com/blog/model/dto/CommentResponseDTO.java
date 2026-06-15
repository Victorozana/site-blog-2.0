package com.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private Long idUser;
    private Long idBlog;
}
