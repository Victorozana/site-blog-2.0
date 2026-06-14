package com.blog.dao;

import com.blog.model.entity.Comment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class CommentDAO implements ICommentDAO, PanacheRepository<Comment> {

}
