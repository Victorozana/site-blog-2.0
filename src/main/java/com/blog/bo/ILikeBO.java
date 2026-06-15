package com.blog.bo;

import com.blog.model.dto.LikeResponseDTO;

public interface ILikeBO {
    void create(Long userId, Long blogId);

    void remove(Long idLike);
}
