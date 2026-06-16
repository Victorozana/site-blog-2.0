package com.blog.bo;

public interface ILikeBO {
    void create(Long userId, Long blogId);

    void remove(Long idLike);
}
