package com.blog.bo;

import com.blog.dao.AuditLogDAO;
import com.blog.dao.BlogDAO;
import com.blog.dao.CommentDAO;
import com.blog.dao.LikeDAO;
import com.blog.dao.UserDAO;
import com.blog.exception.BusinessRuleException;
import com.blog.model.category.UserType;
import com.blog.model.dto.AdminUserDTO;
import com.blog.model.dto.LoginRequestDTO;
import com.blog.model.dto.LoginResponseDTO;
import com.blog.model.dto.UserProfileDTO;
import com.blog.model.dto.UserProfileUpdateDTO;
import com.blog.model.dto.UserRegistrationDTO;
import com.blog.model.dto.UserResponseDTO;
import com.blog.model.entity.Blog;
import com.blog.model.entity.User;
import com.blog.validation.BusinessValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import at.favre.lib.crypto.bcrypt.BCrypt;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.time.Duration;
import java.util.HashSet;

import java.util.List;

@ApplicationScoped
public class UserBO {
    @Inject
    UserDAO userDAO;
    @Inject
    BlogDAO blogDAO;
    @Inject
    CommentDAO commentDAO;
    @Inject
    LikeDAO likeDAO;
    @Inject
    AuditLogDAO auditLogDAO;
    @Inject
    AuditLogBO auditLogBO;

    public User getUserById(Long id) {
        return userDAO.findById(id);
    }

    @Transactional
    public UserResponseDTO saveUser(UserRegistrationDTO dto) {
        BusinessValidator.validateUserRegistration(dto);

        if (dto.getUserType() == null) {
            dto.setUserType(UserType.READER);
        }

        String email = dto.getEmail().trim().toLowerCase();
        User existingUser = userDAO.getUserByEmail(email);

        if (existingUser != null){
            throw new BusinessRuleException("Este email já está cadastrado.");
        }

        String passwordBefore = dto.getPassword().trim();

        String passwordSecurity = BCrypt.withDefaults().hashToString(12, passwordBefore.toCharArray());

        User entity = User.builder()
                .name(dto.getName().trim())
                .lastname(dto.getLastname().trim())
                .email(email)
                .cryptographyPassword(passwordSecurity)
                .userType(dto.getUserType())
                .fone(dto.getFone().trim())
                .dtNasc(dto.getDtNasc())
                .build();

        userDAO.persist(entity);
        auditLogBO.log("USER_REGISTERED", entity, "Novo usuário cadastrado pelo formulário público.");

        return new UserResponseDTO(entity.getId(), entity.getName(), entity.getEmail(), entity.getUserType());
    }

    @Transactional
    public void deleteUser(Long id) {
        BusinessValidator.validatePositiveId(id, "Usuário");

        User user = userDAO.findById(id);

        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
        }

        List<Long> userBlogIds = blogDAO.find("user.id", id)
                .list()
                .stream()
                .map(Blog::getId)
                .toList();

        likeDAO.delete("user.id", id);
        commentDAO.delete("user.id", id);

        if (!userBlogIds.isEmpty()) {
            likeDAO.delete("blog.id in ?1", userBlogIds);
            commentDAO.delete("blog.id in ?1", userBlogIds);
            blogDAO.delete("id in ?1", userBlogIds);
        }

        auditLogDAO.delete("userId", id);
        userDAO.delete(user);
        auditLogBO.log("ACCOUNT_DELETED", null, "Conta excluída definitivamente pelo titular.");
    }

    public LoginResponseDTO login(LoginRequestDTO dto){
        BusinessValidator.validateLogin(dto);

        User entity = userDAO.getUserByEmail(dto.getEmail().trim().toLowerCase());

        //validation with Bcrypt
        if (entity == null || !validation(entity, dto)){
            auditLogBO.log("LOGIN_FAILED", null, dto.getEmail(), null, "Tentativa de login com credenciais inválidas.");
            throw new BusinessRuleException("Credenciais inválidas.");
        }

        String token = Jwt.issuer("http://localhost:8080")
                .upn(entity.getEmail())
                .groups(new HashSet<>(List.of(entity.getUserType().name())))
                .claim("idUser", entity.getId())
                .claim("name", entity.getName())
                .expiresIn(Duration.ofDays(15))
                .sign();

        auditLogBO.log("LOGIN_SUCCESS", entity, "Login realizado com sucesso.");

        return new LoginResponseDTO(token, entity.getName(), entity.getId(), entity.getUserType().name());
    }

    public List<User> listAll() {
        return userDAO.listAll();
    }

    public List<AdminUserDTO> listUsersForAdmin() {
        return userDAO.find("order by createdDateTime desc")
                .list()
                .stream()
                .map(user -> new AdminUserDTO(
                        user.getId(),
                        toFullName(user),
                        user.getEmail(),
                        user.getUserType(),
                        user.getBio(),
                        user.getProfilePictureUrl(),
                        user.getCreatedDateTime()
                ))
                .toList();
    }

    public UserProfileDTO getPublicProfile(Long userId) {
        BusinessValidator.validatePositiveId(userId, "Usuário");

        User user = userDAO.findById(userId);

        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
        }

        return toProfileDTO(user);
    }

    @Transactional
    public UserProfileDTO updateProfile(Long userId, UserProfileUpdateDTO dto) {
        BusinessValidator.validatePositiveId(userId, "Usuário");
        String bio = dto != null ? dto.getBio() : null;
        BusinessValidator.validateBio(bio);

        User user = userDAO.findById(userId);

        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
        }

        user.setBio(bio == null ? null : bio.trim());

        userDAO.persist(user);
        auditLogBO.log("PROFILE_BIO_UPDATED", user, "Bio do perfil atualizada.");

        return toProfileDTO(user);
    }

    private boolean validation(User user, LoginRequestDTO dto){
        String salvePassword = user.getCryptographyPassword();

        BCrypt.Result result = BCrypt.verifyer().verify(dto.getPassword().trim().toCharArray(), salvePassword);

        return result.verified;
    }

    @Transactional
    public UserProfileDTO updateProfilePicture(Long userId, String imageUrl) {
        BusinessValidator.validatePositiveId(userId, "Usuário");
        // 1. Busca o usuário no banco
        User user = userDAO.findById(userId);

        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
        }

        // 2. Atualiza apenas a foto
        user.setProfilePictureUrl(imageUrl);

        // 3. Persiste a alteração
        userDAO.persist(user);
        auditLogBO.log("PROFILE_PICTURE_UPDATED", user, "Foto de perfil atualizada.");

        return toProfileDTO(user);
    }

    private UserProfileDTO toProfileDTO(User user) {
        return new UserProfileDTO(
                user.getId(),
                toFullName(user),
                user.getBio(),
                user.getProfilePictureUrl()
        );
    }

    private String toFullName(User user) {
        if (user.getLastname() == null || user.getLastname().isBlank()) {
            return user.getName();
        }

        return user.getName() + " " + user.getLastname();
    }
}
