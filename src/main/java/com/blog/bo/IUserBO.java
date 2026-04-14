package com.blog.bo;

import com.blog.model.dto.UserLoginDTO;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;
import io.vertx.core.net.impl.pool.Task;
import jakarta.ws.rs.core.Response;

public interface IUserBO {
    User getUserById(Long id);

    Response saveUser(UserRegistrationDTO user);

    Task<Response> login(UserLoginDTO user);

    //void updateUser(User user);

    Response deleteUser(Long id);
}
