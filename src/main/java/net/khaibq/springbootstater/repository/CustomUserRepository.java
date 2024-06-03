package net.khaibq.springbootstater.repository;

import net.khaibq.springbootstater.dto.user.UserDto;
import net.khaibq.springbootstater.dto.user.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {
    Page<UserDto> getPageUser(UserSearchDto dto, Pageable pageable);
}
