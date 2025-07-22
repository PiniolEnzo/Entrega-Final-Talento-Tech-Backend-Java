package com.techlab.service;

import com.techlab.dto.user.*;

import java.util.List;

public interface IUserService {

    List<UserResponse> getAll();

    List<UserDto> findAll();

    UserResponse getById(Long id);

    UserDto findById(Long id);

    UserResponse update(Long id, UpdateUser request);

    void delete(Long id);

    void logicalDelete(Long id);

    void changePassword(Long id, ChangePassword request);

}
