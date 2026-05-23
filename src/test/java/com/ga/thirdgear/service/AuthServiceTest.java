package com.ga.thirdgear.service;

import com.ga.thirdgear.dto.request.LoginRequest;
import com.ga.thirdgear.dto.request.RegisterRequest;
import com.ga.thirdgear.dto.response.AuthResponse;
import com.ga.thirdgear.model.User;
import com.ga.thirdgear.enums.UserRole;
import com.ga.thirdgear.enums.UserStatus;
import com.ga.thirdgear.repository.UserRepository;
import com.ga.thirdgear.security.JwtUtils;
import com.ga.thirdgear.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private EmailService emailService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        authService.setUserRepository(userRepository);
        authService.setPasswordEncoder(passwordEncoder);
        authService.setJwtUtils(jwtUtils);
        authService.setEmailService(emailService);
    }

    @Test
    @DisplayName("Should register new user successfully")
    void shouldRegisterNewUser() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@test.com");
        request.setPassword("John@123");

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("John@123")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(emailService).sendVerificationEmail(anyString(), anyString());

        authService.register(request);

        verify(userRepository).save(any(User.class));
        verify(emailService).sendVerificationEmail(anyString(), anyString());
    }

    @Test
    @DisplayName("Should reject duplicate email")
    void shouldRejectDuplicateEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("exists@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("John@123");

        when(userRepository.findByEmail("exists@test.com"))
                .thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already in use");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should login successfully and return token")
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("John@123");

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@test.com")
                .password("hashed-password")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .build();

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("John@123", "hashed-password")).thenReturn(true);
        when(jwtUtils.generateToken("john@test.com")).thenReturn("mocked-token");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("mocked-token");
        assertThat(response.getEmail()).isEqualTo("john@test.com");
    }

    @Test
    @DisplayName("Should reject wrong password")
    void shouldRejectWrongPassword() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("wrongpassword");

        User user = User.builder()
                .email("john@test.com")
                .password("hashed-password")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .build();

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashed-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid email or password");
    }
}