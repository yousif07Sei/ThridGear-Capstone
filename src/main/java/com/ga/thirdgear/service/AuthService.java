package com.ga.thirdgear.service;

import com.ga.thirdgear.dto.request.LoginRequest;
import com.ga.thirdgear.dto.request.RegisterRequest;
import com.ga.thirdgear.dto.response.AuthResponse;
import com.ga.thirdgear.enums.UserRole;
import com.ga.thirdgear.enums.UserStatus;
import com.ga.thirdgear.model.User;
import com.ga.thirdgear.repository.UserRepository;
import com.ga.thirdgear.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private EmailService emailService;


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }


    public void register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // Generate email verification token
        String verificationToken = UUID.randomUUID().toString();

        // Build and save the new user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .resetToken(verificationToken)
                .build();

        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);
    }


    public AuthResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Check if email is verified
        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        // Check if user is active
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException("Your account has been deactivated");
        }

        // Generate JWT token
        String token = jwtUtils.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }


    public void verifyEmail(String token) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setResetToken(null);
        userRepository.save(user);
    }


    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No account found with that email"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }


    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
    }
}