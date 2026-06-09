package com.blog.bo;

import com.blog.dao.IUserDAO;
import com.blog.exception.BusinessRuleException;
import com.blog.model.dto.LoginRequestDTO;
import com.blog.model.dto.LoginResponseDTO;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import at.favre.lib.crypto.bcrypt.BCrypt;
import io.smallrye.jwt.build.Jwt;

import java.time.Duration;
import java.util.HashSet;
import java.util.Arrays;

import java.util.List;

//@SessionScoped escopo por sessão banco de dados, mantido no servidor
//(geralmente usado em multiplos servidores (microsservicos))

//@RequestScoped criado por requisição na dúvida use esse

//@Dependent CUIDADO!!

//criados e matidos por todo o ciclo de vida da aplicação
//(sempre o mesmo objeto) CUIDADO!! você tem apenas um
//objeto quando essa anotação é usada
//@ApplicationScoped

@RequestScoped
public class UserBO implements IUserBO{
    @Inject
    IUserDAO userDAO;

    @Override
    public User getUserById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    @Transactional
    public Response saveUser(UserRegistrationDTO dto) {
        User existingUser = userDAO.getUserByEmail(dto.getEmail());

        if (existingUser != null){
            throw new BusinessRuleException("This email already exists!");
        }

        String passwordBefore = dto.getPassword();

        String passwordSecurity = BCrypt.withDefaults().hashToString(12, passwordBefore.toCharArray());

        User entity = User.builder()
                .name(dto.getName())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .cryptographyPassword(passwordSecurity)
                .userType(dto.getUserType())
                .fone(dto.getFone())
                .dtNasc(dto.getDtNasc())
                .build();



        userDAO.persist(entity);

        //BO registra a auditoria
        //registrarAuditoria("Cadastro", entity.getEmail());

        return Response.status(Response.Status.CREATED).entity(entity).build();
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
    public Response deleteUser(Long id) {
        userDAO.delete(getUserById(id));
        return Response.status(Response.Status.OK).entity("Excluido com sucesso!").build();
    }

    public LoginResponseDTO login(LoginRequestDTO dto){
        User entity = new User();

        //looking for user with email
        entity = userDAO.getUserByEmail(dto.getEmail());

        //validation with Bcrypt
        if (entity == null || !validation(entity, dto)){
            throw new BusinessRuleException("Invalid credentials!");
        }

        String token = Jwt.issuer("http://localhost:8080")
                .upn(entity.getEmail())
                .groups(new HashSet<>(List.of(entity.getUserType().name())))
                .claim("idUser", entity.getId())
                .claim("name", entity.getName())
                .expiresIn(Duration.ofDays(15))
                .sign();

        return new LoginResponseDTO(token, entity.getName(), entity.getId(), entity.getUserType().name());
    }

    @Override
    public List<User> listAll() {
        return userDAO.listAll();
    }

    private boolean validation(User user, LoginRequestDTO dto){
        String salvePassword = user.getCryptographyPassword();

        BCrypt.Result result = BCrypt.verifyer().verify(dto.getPassword().toCharArray(), salvePassword);

        if (result.verified){
            return true;
        }
        return false;
    }
}