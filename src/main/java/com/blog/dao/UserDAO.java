package com.blog.dao;

import com.blog.model.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDAO implements PanacheRepository<User>{
    public User getUserByEmail(String email) {
        return find("email", email).firstResult();
    }
}