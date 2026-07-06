package com.blog.validation;

import com.blog.exception.BusinessRuleException;
import com.blog.model.category.Category;
import com.blog.model.category.UserType;
import com.blog.model.dto.BlogRegistrationDTO;
import com.blog.model.dto.LoginRequestDTO;
import com.blog.model.dto.UserRegistrationDTO;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public final class BusinessValidator {
    // DEFINE PADRÃO DO EMAIL
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    // DEFINE IDADE MÍNIMA PARA SE CADASTRAR
    private static final int MINIMUM_AGE = 18;
    // MÁXIMO TAMANHO DE IMAGEM POR BYTES
    public static final long MAX_PROFILE_IMAGE_BYTES = 5L * 1024L * 1024L;
    // MÁXIMO TAMANHO DE IMAGEM POR MEGABYTES
    public static final int MAX_PROFILE_IMAGE_MB = 5;
    // MÁXIMO TAMANHO DE TEXTO DO POST. O PostgreSQL TEXT suporta muito mais,
    // mas 50 mil caracteres mantém o blog confortável sem aceitar textos abusivos.
    public static final int MAX_BLOG_CONTENT_CHARACTERS = 50000;

    private BusinessValidator() {
    }

    // valida se o usuário inseriu os dados completos e corretamente
    public static void validateUserRegistration(UserRegistrationDTO dto) {
        if (dto == null) {
            throw new BusinessRuleException("Dados do usuário não foram informados.");
        }

        validatePersonName(dto.getName(), "Nome");
        validatePersonName(dto.getLastname(), "Sobrenome");
        validateBirthDate(dto.getDtNasc());
        validatePhone(dto.getFone());
        validateEmail(dto.getEmail());
        validatePassword(dto.getPassword());

        // segurança para barrar criação de ADMINS
        if (dto.getUserType() == UserType.ADMIN) {
            throw new BusinessRuleException("Administradores não podem ser criados pelo cadastro público.");
        }
    }

    // valida se os dados de login foram inseridos
    public static void validateLogin(LoginRequestDTO dto) {
        if (dto == null) {
            throw new BusinessRuleException("Informe email e senha.");
        }

        // valida email
        validateEmail(dto.getEmail());

        if (isBlank(dto.getPassword())) {
            throw new BusinessRuleException("Senha é obrigatória.");
        }
    }

    // valida se os dados do post foram inseridos
    public static void validateBlogRegistration(BlogRegistrationDTO dto) {
        if (dto == null) {
            throw new BusinessRuleException("Dados do post não foram informados.");
        }

        validateText(dto.getTitle(), "Título", 3, 120);
        validateOptionalText(dto.getSubtitle(), "Subtítulo", 160);
        validateText(dto.getDescription(), "Conteúdo", 20, MAX_BLOG_CONTENT_CHARACTERS);
        validateCategory(dto.getCategory());
    }

    // válida bio
    public static void validateBio(String bio) {
        validateOptionalText(bio, "Bio", 280);
    }

    // válida comentário
    public static void validateComment(String comment) {
        validateText(comment, "Comentário", 2, 1000);
    }

    // válida o ID
    public static void validatePositiveId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new BusinessRuleException(fieldName + " inválido.");
        }
    }

    // válida se existe arquivo e o tipo dele
    public static void validateImageFileName(String fileName) {
        if (isBlank(fileName)) {
            throw new BusinessRuleException("Arquivo de imagem não foi informado.");
        }

        String lowerFileName = fileName.toLowerCase();
        if (!lowerFileName.endsWith(".jpg")
                && !lowerFileName.endsWith(".jpeg")
                && !lowerFileName.endsWith(".png")
                && !lowerFileName.endsWith(".webp")) {
            throw new BusinessRuleException("A foto deve ser JPG, PNG ou WEBP.");
        }
    }

    // válida tamanho de imagem do perfil
    public static void validateProfileImageSize(long sizeInBytes) {
        if (sizeInBytes <= 0) {
            throw new BusinessRuleException("Arquivo de imagem inválido.");
        }

        if (sizeInBytes > MAX_PROFILE_IMAGE_BYTES) {
            throw new BusinessRuleException("A foto de perfil deve ter no máximo " + MAX_PROFILE_IMAGE_MB + " MB.");
        }
    }

    // válida nome
    private static void validatePersonName(String value, String fieldName) {
        validateText(value, fieldName, 2, 60);

        String normalized = value.trim();
        for (int index = 0; index < normalized.length(); index++) {
            char character = normalized.charAt(index);
            if (!Character.isLetter(character) && !Character.isWhitespace(character)) {
                throw new BusinessRuleException(fieldName + " deve conter apenas letras e espaços.");
            }
        }
    }

    // válida data de nascimento
    private static void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new BusinessRuleException("Data de nascimento é obrigatória.");
        }

        LocalDate today = LocalDate.now();
        if (birthDate.isAfter(today)) {
            throw new BusinessRuleException("Data de nascimento não pode ser futura.");
        }

        if (birthDate.isBefore(today.minusYears(120))) {
            throw new BusinessRuleException("Data de nascimento é antiga demais para ser válida.");
        }

        if (Period.between(birthDate, today).getYears() < MINIMUM_AGE) {
            throw new BusinessRuleException("Usuário precisa ter pelo menos " + MINIMUM_AGE + " anos.");
        }
    }

    // válida telefone
    private static void validatePhone(String phone) {
        if (isBlank(phone)) {
            throw new BusinessRuleException("Telefone é obrigatório.");
        }

        // define tamanho mínino para telefone
        String digitsOnly = phone.replaceAll("\\D", "");
        if (digitsOnly.length() < 10 || digitsOnly.length() > 11) {
            throw new BusinessRuleException("Telefone deve ter 10 ou 11 dígitos.");
        }
    }

    // válida email
    private static void validateEmail(String email) {
        if (isBlank(email)) {
            throw new BusinessRuleException("Email é obrigatório.");
        }

        // válida padrão email
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new BusinessRuleException("Email inválido.");
        }
    }

    // válida senha, tamanho mínimo 6 caracteres até 72
    private static void validatePassword(String password) {
        if (isBlank(password)) {
            throw new BusinessRuleException("Senha é obrigatória.");
        }

        if (password.length() < 6 || password.length() > 72) {
            throw new BusinessRuleException("Senha deve ter entre 6 e 72 caracteres.");
        }
    }

    // válida categoria
    private static void validateCategory(Category category) {
        if (category == null) {
            throw new BusinessRuleException("Categoria é obrigatória.");
        }
    }

    // válida textos obrigatórios
    private static void validateText(String value, String fieldName, int minLength, int maxLength) {
        if (isBlank(value)) {
            throw new BusinessRuleException(fieldName + " é obrigatório.");
        }

        String normalized = value.trim();
        if (normalized.length() < minLength) {
            throw new BusinessRuleException(fieldName + " deve ter pelo menos " + minLength + " caracteres.");
        }

        if (normalized.length() > maxLength) {
            throw new BusinessRuleException(fieldName + " deve ter no máximo " + maxLength + " caracteres.");
        }
    }

    // válida textos opcionais
    private static void validateOptionalText(String value, String fieldName, int maxLength) {
        if (value != null && value.trim().length() > maxLength) {
            throw new BusinessRuleException(fieldName + " deve ter no máximo " + maxLength + " caracteres.");
        }
    }

    // valida se o campo está vazio
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
