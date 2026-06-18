package com.blog.bo;

import com.blog.model.dto.CommentRequestDTO;

public interface ICommentBO {
    void create(Long userId, Long blogId, CommentRequestDTO dto);

    void remove(Long commentId);
}
