package net.khaibq.springbootstater.dto.auth;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginReqDto {
    @NotNull(message = "username is required")
    private String username;
    
    @NotNull(message = "password is required")
    private String password;
}
