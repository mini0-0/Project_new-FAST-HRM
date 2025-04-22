package com.project.fasthrm.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {
    MASTER("ROLE_MASTER", "관리자"),
    WORKER("ROLE_WORKER", "직원"),
    MEMBER("ROLE_MEMBER", "회원");

    @Getter private final String roleName;
    @Getter private final String description;
}