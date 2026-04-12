package com.blog.bo;

import com.blog.dao.IUserDAO;
import com.blog.dao.UserDAO;
import com.blog.model.dto.UserLoginDTO;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserBO implements IUserBO{
    @Inject
    IUserDAO userDAO;

    public UserBO(){
        this.userDAO = new UserDAO();
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
        entity.setDtNasc(dto.getDtNasc());
        entity.setFone(dto.getFone());
        entity.setEmail(dto.getEmail());
        entity.setUserType(dto.getUserType());
        entity.setCryptographyPassword(dto.getCryptographyPassword());
        System.out.println("PASSWORD: " + entity.getCryptographyPassword());
        //BO chama o DAO passando a Entity
        userDAO.persist(entity);

        //BO registra a auditoria
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

//    public boolean login(UserLoginDTO){
//
//    }
}