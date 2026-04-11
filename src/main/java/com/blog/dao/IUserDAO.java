package com.blog.dao;

import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface IUserDAO extends PanacheRepository<User> {
//    List<User> listAllUsers();
//
//    User getUserById(int id);
//
//    void saveUser(User user);
//
//    void updateUser(User user);
//
//    void deleteUser(int id);
}
