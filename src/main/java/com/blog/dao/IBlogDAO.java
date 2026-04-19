package com.blog.dao;

import com.blog.model.entity.Blog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface IBlogDAO extends PanacheRepository<Blog>{
}
