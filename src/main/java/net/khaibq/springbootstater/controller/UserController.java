package net.khaibq.springbootstater.controller;

import lombok.RequiredArgsConstructor;
import net.khaibq.springbootstater.dto.BaseResponse;
import net.khaibq.springbootstater.dto.user.UserDto;
import net.khaibq.springbootstater.dto.user.UserSearchDto;
import net.khaibq.springbootstater.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public BaseResponse<Page<UserDto>> getPageUser(UserSearchDto dto, Pageable pageable) {
        return BaseResponse.success(userService.getPageUser(dto, pageable));
    }
}
