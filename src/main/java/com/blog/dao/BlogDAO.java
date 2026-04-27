package com.blog.dao;

import com.blog.model.entity.Blog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BlogDAO implements IBlogDAO, PanacheRepository<Blog>{
}
