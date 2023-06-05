package com.golub.school.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, MENTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
