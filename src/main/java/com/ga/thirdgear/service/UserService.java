package com.ga.thirdgear.service;

import com.ga.thirdgear.enums.UserStatus;
import com.ga.thirdgear.exception.InformationNotFoundException;
import com.ga.thirdgear.model.User;
import com.ga.thirdgear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private  UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setProfilePicture(updatedUser.getProfilePicture());
        return userRepository.save(user);
    }

    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("User not found with id: " + id));
        user.setStatus(UserStatus.INACTIVE);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void changePassword(String userEmail, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User updateProfile(String userEmail, User updatedUser) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        return userRepository.save(user);
    }

    public User uploadProfilePicture(String userEmail, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        // Create upload directory if it doesn't exist
        String uploadDir = "uploads/profiles/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        user.setProfilePicture(uploadDir + fileName);
        return userRepository.save(user);
    }

    public User getProfile(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));
    }




}
