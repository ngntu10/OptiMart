package com.Optimart.enums;

public enum RoleNameEnum {
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    RoleNameEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
