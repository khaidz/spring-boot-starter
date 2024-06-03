package net.khaibq.springbootstater.service.impl;

import lombok.RequiredArgsConstructor;
import net.khaibq.springbootstater.dto.auth.RegisterReqDto;
import net.khaibq.springbootstater.dto.user.UserDto;
import net.khaibq.springbootstater.dto.user.UserSearchDto;
import net.khaibq.springbootstater.entity.User;
import net.khaibq.springbootstater.exception.BaseException;
import net.khaibq.springbootstater.repository.CustomUserRepository;
import net.khaibq.springbootstater.repository.UserRepository;
import net.khaibq.springbootstater.service.UserService;
import net.khaibq.springbootstater.utils.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserRepository customUserRepository;

    @Override
    public Long registerUser(RegisterReqDto dto) {
        if (userRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            throw new BaseException("username is already in use");
        }
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new BaseException("email is already in use");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole(RoleEnum.USER.getName());
        user.setIsDeleted(0);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public Page<UserDto> getPageUser(UserSearchDto dto, Pageable pageable) {
        return customUserRepository.getPageUser(dto, pageable);
    }
}
