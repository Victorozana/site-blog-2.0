package com.blog.dao;

import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;

import java.util.List;
import java.util.Map;

@RequestScoped
public class UserDAO implements IUserDAO {
    @Inject
    IUserDAO userDAO;

    @Override
    public User getUserByEmail(String email) {
        return find("email", email).firstResult();
    }
}