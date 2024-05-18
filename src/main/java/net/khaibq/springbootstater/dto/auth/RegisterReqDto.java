package net.khaibq.springbootstater.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterReqDto {
    @NotBlank
    @Length(min = 3, max = 50, message = "username must be between 3 and 50 characters")
    private String username;

    @NotNull
    @Length(min = 3, message = "password must be at least 3 characters")
    private String password;

    @NotNull
    @Email(message = "email format is invalid")
    private String email;
}
