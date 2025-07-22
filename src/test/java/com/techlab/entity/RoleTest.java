package com.techlab.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleTest {
    @Test
    @DisplayName("Set and get Role name")
    void setAndGetRoleName() {
        Role role = Role.ADMIN;
        Assertions.assertEquals("ADMIN", role.name());
    }
}

