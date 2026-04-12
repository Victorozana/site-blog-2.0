package com.blog.bo;

import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;

public interface IUserBO {
    User getUserById(Long id);

    void saveUser(UserRegistrationDTO user);

    //void updateUser(User user);

    void deleteUser(Long id);
}
