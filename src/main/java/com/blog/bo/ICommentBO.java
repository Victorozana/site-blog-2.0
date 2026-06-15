package com.blog.bo;

import com.blog.model.dto.CommentResponseDTO;

public interface ICommentBO {
    void create(Long userId, Long blogId);

    void remove(Long commentId);
}
