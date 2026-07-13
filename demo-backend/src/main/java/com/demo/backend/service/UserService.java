package com.demo.backend.service;

import com.demo.backend.dto.UserCreateDTO;
import com.demo.backend.dto.UserUpdateDTO;
import com.demo.backend.entity.User;
import com.demo.backend.exception.BusinessException;
import com.demo.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "user not found: " + id));
    }

    public User create(UserCreateDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return userRepository.save(user);
    }

    public User update(Long id, UserUpdateDTO dto) {
        User existing = findById(id);
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setAge(dto.getAge());
        return userRepository.save(existing);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException(404, "user not found: " + id);
        }
        userRepository.deleteById(id);
    }
}