package com.blog.dao;

import com.blog.model.dto.UserLoginDTO;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UserDAO implements IUserDAO, PanacheRepository<User>{
    @Override
    public User getUserByEmail(String email) {
        return find("email", email).firstResult();
    }
}