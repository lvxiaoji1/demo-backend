package com.demo.backend.service;

import com.demo.backend.dto.UserCreateDTO;
import com.demo.backend.dto.UserUpdateDTO;
import com.demo.backend.entity.User;
import com.demo.backend.exception.BusinessException;
import com.demo.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;

    @Test
    void createUser_shouldSaveAndReturnUser() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setName("test");
        dto.setEmail("test@test.com");
        dto.setAge(25);

        User saved = new User();
        saved.setId(1L); saved.setName("test");
        saved.setEmail("test@test.com"); saved.setAge(25);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.create(dto);
        assertEquals("test", result.getName());
        assertEquals("test@test.com", result.getEmail());
        assertEquals(25, result.getAge());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findById_notFound_shouldThrow() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> userService.findById(999L));
    }

    @Test
    void updateUser_shouldUpdateFields() {
        User u = new User(); u.setId(1L);
        u.setName("old"); u.setEmail("old@test.com"); u.setAge(20);
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setName("new"); dto.setEmail("new@test.com"); dto.setAge(30);
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User r = userService.update(1L, dto);
        assertEquals("new", r.getName());
    }

    @Test
    void deleteUser_notFound_shouldThrow() {
        when(userRepository.existsById(999L)).thenReturn(false);
        assertThrows(BusinessException.class, () -> userService.delete(999L));
    }
}