package com.blog.validation;

import com.blog.exception.BusinessRuleException;
import com.blog.model.category.Category;
import com.blog.model.category.UserType;
import com.blog.model.dto.BlogRegistrationDTO;
import com.blog.model.dto.LoginRequestDTO;
import com.blog.model.dto.UserRegistrationDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BusinessValidatorTest {

    @Test
    void shouldAcceptValidUserRegistration() {
        UserRegistrationDTO dto = validUser();

        assertDoesNotThrow(() -> BusinessValidator.validateUserRegistration(dto));
    }

    @Test
    void shouldRejectNameWithNumber() {
        UserRegistrationDTO dto = validUser();
        dto.setName("Victor2");

        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateUserRegistration(dto));
    }

    @Test
    void shouldRejectInvalidBirthDate() {
        UserRegistrationDTO futureDate = validUser();
        futureDate.setDtNasc(LocalDate.now().plusDays(1));

        UserRegistrationDTO tooYoung = validUser();
        tooYoung.setDtNasc(LocalDate.now().minusYears(10));

        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateUserRegistration(futureDate));
        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateUserRegistration(tooYoung));
    }

    @Test
    void shouldRejectAdminFromPublicRegistration() {
        UserRegistrationDTO dto = validUser();
        dto.setUserType(UserType.ADMIN);

        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateUserRegistration(dto));
    }

    @Test
    void shouldRejectInvalidLogin() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("email-invalido");
        dto.setPassword("123456");

        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateLogin(dto));
    }

    @Test
    void shouldAcceptValidBlogRegistration() {
        BlogRegistrationDTO dto = validBlog();

        assertDoesNotThrow(() -> BusinessValidator.validateBlogRegistration(dto));
    }

    @Test
    void shouldRejectBlogWithoutContent() {
        BlogRegistrationDTO dto = validBlog();
        dto.setDescription("curto");

        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateBlogRegistration(dto));
    }

    @Test
    void shouldRejectBlogContentAboveLimit() {
        BlogRegistrationDTO dto = validBlog();
        dto.setDescription("a".repeat(BusinessValidator.MAX_BLOG_CONTENT_CHARACTERS + 1));

        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateBlogRegistration(dto));
    }

    @Test
    void shouldRejectInvalidComment() {
        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateComment(" "));
        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateComment("a"));
    }

    @Test
    void shouldRejectInvalidIds() {
        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validatePositiveId(null, "Post"));
        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validatePositiveId(0L, "Post"));
    }

    @Test
    void shouldValidateProfileFields() {
        assertDoesNotThrow(() -> BusinessValidator.validateBio("Bio simples."));
        assertDoesNotThrow(() -> BusinessValidator.validateBio("a".repeat(280)));
        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateBio("a".repeat(281)));
        assertDoesNotThrow(() -> BusinessValidator.validateImageFileName("avatar.png"));
        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateImageFileName("script.exe"));
        assertDoesNotThrow(() -> BusinessValidator.validateProfileImageSize(BusinessValidator.MAX_PROFILE_IMAGE_BYTES));
        assertThrows(BusinessRuleException.class, () -> BusinessValidator.validateProfileImageSize(BusinessValidator.MAX_PROFILE_IMAGE_BYTES + 1));
    }

    private UserRegistrationDTO validUser() {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setName("José Victor");
        dto.setLastname("Oliveira Silva");
        dto.setDtNasc(LocalDate.now().minusYears(20));
        dto.setFone("(61)99849-2225");
        dto.setEmail("victor@example.com");
        dto.setPassword("123456");
        dto.setUserType(UserType.READER);
        return dto;
    }

    private BlogRegistrationDTO validBlog() {
        BlogRegistrationDTO dto = new BlogRegistrationDTO();
        dto.setTitle("Título válido");
        dto.setSubtitle("Subtítulo opcional");
        dto.setDescription("Conteúdo válido com mais de vinte caracteres.");
        dto.setCategory(Category.advices);
        return dto;
    }
}
