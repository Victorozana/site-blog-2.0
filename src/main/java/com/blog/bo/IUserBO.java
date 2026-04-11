package com.blog.bo;

import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;

import java.util.List;

public interface IUserBO {
    List<User> listAllUsers();

    User getUserById(Long id);

    void saveUser(UserRegistrationDTO user);

//    void updateUser(User user);

    void deleteUser(Long id);
}
