package com.blog.bo;

import com.blog.dao.IUserDAO;
import com.blog.model.dto.UserLoginDTO;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.dto.UserResponseDTO;
import com.blog.model.entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        User entity = User.builder()
                .name(dto.getName())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .cryptographyPassword(dto.getCryptographyPassword())
                .userType(dto.getUserType())
                .fone(dto.getFone())
                .dtNasc(dto.getDtNasc())
                .build();

        userDAO.persist(entity);

        //   UserResponseDTO responseDTO = UserResponseDTO.builder();

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

    public Response login(UserLoginDTO dto){
        User entity = new User();
        entity = userDAO.getUserByEmail(dto.getEmail());
        if (!validacaoLogin(entity, dto)){
            return Response.status(Response.Status.UNAUTHORIZED).entity("Dados inváliddos!").build();
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setEmail(entity.getEmail());
        userResponseDTO.setName(entity.getName());
        userResponseDTO.setUserType(entity.getUserType());

        return Response.status(Response.Status.ACCEPTED).entity("Logado com sucesso").build();
    }

    @Override
    public List<User> listAll() {
        return userDAO.listAll();
    }

    private boolean validacaoLogin(User user, UserLoginDTO dto){
        if (user == null || dto == null){
            return false;
        }
        return Objects.equals(user.getEmail(), dto.getEmail()) && Objects.equals(user.getCryptographyPassword(), dto.getCryptographyPassword());
    }
}