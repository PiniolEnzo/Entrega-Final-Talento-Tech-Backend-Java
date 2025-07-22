package com.techlab.controller;

import com.techlab.dto.auth.AuthResponse;
import com.techlab.dto.user.*;
import com.techlab.mapper.UserMapper;
import com.techlab.service.IAuthService;
import com.techlab.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;
    private final IAuthService authService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.findById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody UpdateUser request
            ){
        return ResponseEntity.ok().body(userService.update(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword (
            @PathVariable Long id,
            @Valid @RequestBody ChangePassword request){

        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

}
