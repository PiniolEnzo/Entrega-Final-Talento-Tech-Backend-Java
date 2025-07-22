package com.techlab.service.implementation;

import com.techlab.dto.user.*;
import com.techlab.entity.Role;
import com.techlab.entity.User;
import com.techlab.exception.DuplicateUserException;
import com.techlab.exception.UserNotFoundException;
import com.techlab.mapper.UserMapper;
import com.techlab.repository.IUserRepository;
import com.techlab.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAll() {
        return UserMapper.toUserResponse(userRepository.findByActiveTrue());
    }

    @Override
    public List<UserDto> findAll() {
        return UserMapper.toUserDto(userRepository.findAll());
    }

    @Override
    public UserResponse getById(Long id) {
        return UserMapper.toUserResponse(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserDto findById(Long id) {
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserResponse update(Long id, UpdateUser request) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setName(request.getName());
        userRepository.save(user);

        return UserMapper.toUserResponse(user);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    @Override
    public void logicalDelete(Long id){
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setActive(false);
    }

    @Override
    public void changePassword(Long id, ChangePassword request) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AccessDeniedException("Password does not match.");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
}
