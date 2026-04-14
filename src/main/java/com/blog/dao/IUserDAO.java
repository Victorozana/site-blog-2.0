package com.blog.dao;

import com.blog.model.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface IUserDAO extends PanacheRepository<User> {
    User getUserByEmail(String email);
}
