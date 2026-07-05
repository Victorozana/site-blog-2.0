package com.blog.dao;

import com.blog.model.entity.Blog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogDAO implements PanacheRepository<Blog>{
}