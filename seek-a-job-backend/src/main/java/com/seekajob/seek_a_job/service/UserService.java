package com.seekajob.seek_a_job.service;

import com.seekajob.seek_a_job.dto.UserRegistrationDto;
import com.seekajob.seek_a_job.entity.User;
import com.seekajob.seek_a_job.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(UserRegistrationDto registrationDto) {
        // Check if user already exists
        userRepository.findByEmail(registrationDto.getEmail()).ifPresent(u -> {
            throw new IllegalStateException("User with email " + registrationDto.getEmail() + " already exists.");
        });

        User newUser = new User();
        newUser.setEmail(registrationDto.getEmail());
        // Encode the password before saving
        newUser.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        newUser.setRole(registrationDto.getRole());

        return userRepository.save(newUser);
    }
}
