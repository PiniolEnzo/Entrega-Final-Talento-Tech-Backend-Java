package com.techlab.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

class UserTest {
    @Test
    @DisplayName("Set and get User name and password")
    void setAndGetUserEmailAndPassword() {
        User user = new User();
        user.setName("Duki");
        user.setPassword("secret");
        Assertions.assertEquals("Duki", user.getName());
        Assertions.assertEquals("secret", user.getPassword());
    }

    @Test
    @DisplayName("Set and get all User properties")
    void setAndGetAllUserProperties() {
        User user = new User();
        user.setId(1L);
        user.setName("testuser");
        user.setPassword("pass123");
        user.setActive(true);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("testuser", user.getName());
        Assertions.assertEquals("pass123", user.getPassword());
        Assertions.assertTrue(user.isActive());
        Assertions.assertEquals(now, user.getCreatedAt());
        Assertions.assertEquals(now, user.getUpdatedAt());
    }

    @Test
    @DisplayName("User constructors work as expected")
    void userConstructorsWork() {
        User u1 = new User("name1", "pwd1", true);
        Assertions.assertEquals("name1", u1.getName());
        Assertions.assertEquals("pwd1", u1.getPassword());
        Assertions.assertTrue(u1.isActive());
        Role role = Role.USER;
        User u2 = new User("name2", "pwd2", false, role);
        Assertions.assertEquals("name2", u2.getName());
        Assertions.assertEquals("pwd2", u2.getPassword());
        Assertions.assertFalse(u2.isActive());
        Assertions.assertEquals(role, u2.getUserRole());
    }

}
