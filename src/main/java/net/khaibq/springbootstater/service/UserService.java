package net.khaibq.springbootstater.service;

import net.khaibq.springbootstater.dto.auth.RegisterReqDto;
import net.khaibq.springbootstater.dto.user.UserDto;
import net.khaibq.springbootstater.dto.user.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Long registerUser(RegisterReqDto dto);

    Page<UserDto> getPageUser(UserSearchDto dto, Pageable pageable);
}
