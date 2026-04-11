package com.blog.bo;

import com.blog.bo.IUserBO;
import com.blog.dao.IUserDAO;
import com.blog.dao.UserDAO;
import com.blog.model.UserType;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserBO implements IUserBO{
    @Inject
    IUserDAO userDAO;

    public UserBO(){
        this.userDAO = new UserDAO();
    }

    @Override
    public List<User> listAllUsers() {
        return userDAO.listAll();
    }

    @Override
    public User getUserById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    @Transactional
    public void saveUser(UserRegistrationDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setDt_nasc(dto.getDt_nasc());
        entity.setFone(dto.getFone());
        entity.setEmail(dto.getEmail());
        entity.setUser_type(dto.getUser_type());
        entity.setcryptography_password(dto.getcryptography_password());

        // 2. O BO chama o DAO passando a Entity
        userDAO.persist(entity);

        // 3. O BO registra a auditoria (Requisito 6)
        //registrarAuditoria("Cadastro", entity.getEmail());
    }

//    @Override
//    public void updateUser(User user) {
//        var query = "UPDATE USER" +
//                " SET USERNAME = :username " +
//                " SET PASSWORD = :password " +
//                " SET  EMAIL = :email " +
//                " SET  ROLE = :role " +
//                " WHERE ID = :id";
//
//        var id = user.getId();
//
//        userDAO.update(query, id);
//    }

    @Override
    public void deleteUser(Long id) {
        userDAO.delete(getUserById(id));
    }
}