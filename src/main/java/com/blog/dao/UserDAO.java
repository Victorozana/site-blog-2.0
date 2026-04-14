package com.blog.dao;

import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UserDAO implements IUserDAO {
    @Inject
    IUserDAO userDAO;

    @Override
    public User getUserByEmail(String email) {
        var query = "SELECT * " +
                    "FROM users " +
                    "WHERE email = :email;";

        return userDAO.find(query, Map.of("email", email)).firstResult();
    }
}