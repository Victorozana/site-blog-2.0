package com.blog.bo;

import com.blog.model.dto.LoginRequestDTO;
import com.blog.model.dto.LoginResponseDTO;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.dto.UserResponseDTO;
import com.blog.model.entity.User;
import jakarta.ws.rs.core.Response;

import java.util.List;

public interface IUserBO {
    User getUserById(Long id);

    UserResponseDTO saveUser(UserRegistrationDTO user);

    LoginResponseDTO login(LoginRequestDTO user);

    //void updateUser(User user);

    List<User> listAll();

    void deleteUser(Long id);
}
