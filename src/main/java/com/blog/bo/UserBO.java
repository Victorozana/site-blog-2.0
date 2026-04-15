package com.blog.bo;

import com.blog.dao.UserDAO;
import com.blog.model.dto.UserLoginDTO;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.dto.UserResponseDTO;
import com.blog.model.entity.User;
import io.vertx.core.net.impl.pool.Task;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

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
    UserDAO userDAO;

    @Override
    public User getUserById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    @Transactional
    public Response saveUser(UserRegistrationDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setDtNasc(dto.getDtNasc());
        entity.setFone(dto.getFone());
        entity.setEmail(dto.getEmail());
        entity.setUserType(dto.getUserType());
        entity.setCryptographyPassword(dto.getCryptographyPassword());
        System.out.println("dt nasc: " + dto.getDtNasc());
        System.out.println("PASSWORD: " + entity.getCryptographyPassword());
        //BO chama o DAO passando a Entity
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

    public Response login(UserLoginDTO dto){
        User entity = new User();
        entity = userDAO.getUserByEmail(dto.getEmail());
        if (validacaoLogin(entity, dto) == false){
            return Response.status(Response.Status.UNAUTHORIZED).entity("Dados inváliddos!").build();
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setEmail(entity.getEmail());
        userResponseDTO.setName(entity.getName());
        userResponseDTO.setUserType(entity.getUserType());

        return Response.status(Response.Status.ACCEPTED).entity("Logado com sucesso").build();
    }

    private boolean validacaoLogin(User user, UserLoginDTO dto){
        if (user == null || dto == null){
            return false;
        }
        return Objects.equals(user.getEmail(), dto.getEmail()) && Objects.equals(user.getCryptographyPassword(), dto.getCryptographyPassword());
    }
}