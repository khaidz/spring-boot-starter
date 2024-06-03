package net.khaibq.springbootstater.dto.user;

import lombok.Data;

// Đặt tên biến thêm us đằng trước để test việc mapping thôi
@Data
public class UserDto {
    private Long usId;
    private String usUsername;
    private String usPassword;
    private String usEmail;
    private String usRole;
}
