package com.seekajob.seek_a_job.controller;

import com.seekajob.seek_a_job.dto.JwtResponseDto;
import com.seekajob.seek_a_job.dto.LoginRequestDto;
import com.seekajob.seek_a_job.dto.UserRegistrationDto;
import com.seekajob.seek_a_job.entity.JobSeekerProfile;
import com.seekajob.seek_a_job.entity.RecruiterProfile;
import com.seekajob.seek_a_job.entity.User;
import com.seekajob.seek_a_job.service.JobSeekerProfileService;
import com.seekajob.seek_a_job.service.JwtService;
import com.seekajob.seek_a_job.service.RecruiterProfileService;
import com.seekajob.seek_a_job.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final RecruiterProfileService recruiterProfileService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        // If authentication is successful, the user exists. We can now load the full
        // UserDetails object (which includes roles) to generate a complete token.
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDto(jwtToken));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        User savedUser = userService.createUser(registrationDto);
        if (savedUser.getRole() == User.Role.JOB_SEEKER) {
            jobSeekerProfileService.createProfile(savedUser.getId(), new JobSeekerProfile());
        } else if (savedUser.getRole() == User.Role.RECRUITER) {
            recruiterProfileService.createProfile(savedUser.getId(), new RecruiterProfile());
        }
        return new ResponseEntity<>(Map.of("message", "User registered successfully!", "userId", savedUser.getId()), HttpStatus.CREATED);
    }
}