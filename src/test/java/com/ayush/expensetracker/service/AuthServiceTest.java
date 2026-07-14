package com.ayush.expensetracker.service;

import com.ayush.expensetracker.dto.AuthResponse;
import com.ayush.expensetracker.dto.LoginRequest;
import com.ayush.expensetracker.dto.RegisterRequest;
import com.ayush.expensetracker.entity.User;
import com.ayush.expensetracker.exception.DuplicateResourceException;
import com.ayush.expensetracker.exception.InvalidCredentials;
import com.ayush.expensetracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerSuccess() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Ayush");
        request.setEmail("ayush@gmail.com");
        request.setPassword("password");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password"))
                .thenReturn("encodedPassword");

        authService.register(request);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("Ayush", savedUser.getName());
        assertEquals("ayush@gmail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
    }
    @Test
    void registerDuplicateEmail() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("ayush@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(new User()));

        assertThrows(
                DuplicateResourceException.class,
                () -> authService.register(request)
        );

        verify(userRepository, never()).save(any());
    }
    @Test
    void loginSuccess() {

        LoginRequest request = new LoginRequest();
        request.setEmail("ayush@gmail.com");
        request.setPassword("password");

        User user = User.builder()
                .email("ayush@gmail.com")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password",
                "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        AuthResponse response =
                authService.login(request);

        assertEquals("jwt-token", response.getToken());
    }
    @Test
    void loginInvalidEmail() {

        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentials.class,
                () -> authService.login(request)
        );
    }
    @Test
    void loginInvalidPassword() {

        LoginRequest request = new LoginRequest();
        request.setEmail("ayush@gmail.com");
        request.setPassword("wrong");

        User user = User.builder()
                .email("ayush@gmail.com")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrong",
                "encodedPassword"))
                .thenReturn(false);

        assertThrows(
                InvalidCredentials.class,
                () -> authService.login(request)
        );
    }
}
