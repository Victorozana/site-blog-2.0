package com.blog.bo;

public interface ICommentBO {
    void create(Long userId, Long blogId);

    void remove(Long commentId);
}
