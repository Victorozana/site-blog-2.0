package com.blog.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    // O front-end precisará enviar um JSON assim: { "text": "Meu comentário" }
    private String comment;
}