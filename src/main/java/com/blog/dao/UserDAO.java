package com.blog.dao;

import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class UserDAO implements IUserDAO {
    private IUserDAO userDAO;

    public List<User> listAllUsers() {
        return userDAO.listAll();
    }

    public User getUserById(Long id) {
        return userDAO.findById(id);
    }

    public void deleteUser(Long id) {
        userDAO.delete(getUserById(id));
    }
}
