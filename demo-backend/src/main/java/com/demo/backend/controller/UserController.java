package com.demo.backend.controller;

import com.demo.backend.dto.ApiResponse;
import com.demo.backend.dto.UserCreateDTO;
import com.demo.backend.dto.UserResponse;
import com.demo.backend.dto.UserUpdateDTO;
import com.demo.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<UserResponse> create(@Valid @RequestBody UserCreateDTO dto) {
        return ApiResponse.success(UserResponse.from(userService.create(dto)));
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> findAll() {
        List<UserResponse> list = userService.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success(list);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(UserResponse.from(userService.findById(id)));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        return ApiResponse.success(UserResponse.from(userService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success(null);
    }
}