package com.blog.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponseDTO {
    private Long totalLikes;
    private boolean userLiked;
}
