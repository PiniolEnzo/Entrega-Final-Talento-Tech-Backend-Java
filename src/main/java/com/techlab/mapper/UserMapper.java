package com.techlab.mapper;

import com.techlab.dto.user.RegisterRequest;
import com.techlab.dto.user.UserDto;
import com.techlab.dto.user.UserResponse;
import com.techlab.entity.Role;
import com.techlab.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    // user response
    public static UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        return response;
    }

    public static List<UserResponse> toUserResponse(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    // user dto
    public static UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setActive(user.isActive());
        dto.setUserRole(user.getUserRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public static List<UserDto> toUserDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }


    // register request
    public static User toUser(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setUserRole(Role.USER);
        user.setActive(true);
        return user;
    }

}
