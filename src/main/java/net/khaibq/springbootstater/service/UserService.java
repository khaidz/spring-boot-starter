package net.khaibq.springbootstater.service;

import net.khaibq.springbootstater.dto.auth.RegisterReqDto;

public interface UserService {

    Long registerUser(RegisterReqDto dto);
}
