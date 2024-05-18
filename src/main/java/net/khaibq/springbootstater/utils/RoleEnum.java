package net.khaibq.springbootstater.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");
    private final String name;
}
