package es.upm.miw.companyds.tfm_spring.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.api.dto.validation.Validations;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @NotBlank
    @Pattern(regexp = Validations.VALID_PHONE)
    private String phone;
    @NotBlank
    private String firstName;
    @NotBlank
    private String familyName;
    @NotBlank
    @Pattern(regexp = Validations.VALID_EMAIL)
    private String email;
    @NotBlank
    private String dni;
    @NotBlank
    private String password;
    private Role role;

    public void doDefault() {
        if (Objects.isNull(password)) {
            password = UUID.randomUUID().toString();
        }
        if (Objects.isNull(role)) {
            this.role = Role.CUSTOMER;
        }
    }

    public static UserDto ofUser(User user) {
        return UserDto.builder()
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .familyName(user.getFamilyName())
                .email(user.getEmail())
                .dni(user.getDni())
                .role(user.getRole())
                .build();
    }

    public User toUser() {
        this.doDefault();
        User user = new User();
        BeanUtils.copyProperties(this, user);
        user.setPassword(new BCryptPasswordEncoder().encode(this.password));
        return user;
    }
}