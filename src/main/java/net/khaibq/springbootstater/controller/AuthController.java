package net.khaibq.springbootstater.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.khaibq.springbootstater.config.jwt.JwtUtils;
import net.khaibq.springbootstater.dto.BaseResponse;
import net.khaibq.springbootstater.dto.auth.LoginReqDto;
import net.khaibq.springbootstater.dto.auth.LoginResDto;
import net.khaibq.springbootstater.dto.auth.RegisterReqDto;
import net.khaibq.springbootstater.service.UserService;
import net.khaibq.springbootstater.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;

    @Value("${app.token.jwtExpirationMs}")
    private int jwtExpirationMs;

    @PostMapping("/login")
    public BaseResponse<LoginResDto> handleLogin(@RequestBody @Valid LoginReqDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        if (authentication.isAuthenticated()) {
            String token = jwtUtils.generateToken(authentication);
            redisTemplate.opsForValue().set("TOKEN_" + dto.getUsername().toUpperCase(), token, jwtExpirationMs, TimeUnit.MILLISECONDS);
            LoginResDto result = new LoginResDto(token);
            return BaseResponse.success(result);
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @PostMapping("/register")
    public BaseResponse<Long> handleRegister(@RequestBody @Valid RegisterReqDto dto) {
        return BaseResponse.success(userService.registerUser(dto));
    }

    @GetMapping("/logout")
    public BaseResponse<Void> handleLogout() {
        String username = SecurityUtils.getCurrentUsername().orElse("");
        String key = "TOKEN_" + username.toUpperCase();
        redisTemplate.opsForValue().set(key, "", 1, TimeUnit.MILLISECONDS);
        return BaseResponse.success(null);
    }
}
