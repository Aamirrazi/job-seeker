package com.seekajob.seek_a_job.controller;

import com.seekajob.seek_a_job.dto.JobSeekerProfileDto;
import com.seekajob.seek_a_job.dto.RecruiterProfileDto;
import com.seekajob.seek_a_job.dto.JobSeekerProfileResponseDto;
import com.seekajob.seek_a_job.dto.RecruiterProfileResponseDto;
import com.seekajob.seek_a_job.entity.JobSeekerProfile;
import com.seekajob.seek_a_job.entity.RecruiterProfile;
import com.seekajob.seek_a_job.entity.User;
import com.seekajob.seek_a_job.exception.ResourceNotFoundException;
import com.seekajob.seek_a_job.service.JobSeekerProfileService;
import com.seekajob.seek_a_job.dto.JobResponseDto;
import com.seekajob.seek_a_job.service.ApplicationService;
import com.seekajob.seek_a_job.repository.UserRepository;
import com.seekajob.seek_a_job.mapper.ProfileMapper;
import com.seekajob.seek_a_job.service.RecruiterProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private final RecruiterProfileService recruiterProfileService;
    private final ProfileMapper profileMapper;
    private final UserRepository userRepository;
    private final ApplicationService applicationService;

    /**
     * Gets the profile for the currently authenticated user.
     * This provides a convenient way for a user to fetch their own data without knowing their ID.
     * @param principal The authenticated user.
     * @return The user's profile DTO (JobSeeker or Recruiter).
     */
    @GetMapping("/me/profile")
    public ResponseEntity<?> getMyProfile(Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Authentication is required to fetch your profile.");
        }
        User authenticatedUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found with email: " + principal.getName()));

        if (authenticatedUser.getRole() == User.Role.JOB_SEEKER) {
            JobSeekerProfile profile = jobSeekerProfileService.getProfileByUserId(authenticatedUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found for current user."));
            return ResponseEntity.ok(profileMapper.toDto(profile));
        } else if (authenticatedUser.getRole() == User.Role.RECRUITER) {
            RecruiterProfile profile = recruiterProfileService.getProfileByUserId(authenticatedUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found for current user."));
            return ResponseEntity.ok(profileMapper.toDto(profile));
        } else {
            // This case should ideally not be reachable if roles are handled correctly.
            return ResponseEntity.badRequest().body("User has an undefined role.");
        }
    }

    @GetMapping("/me/applications")
    public ResponseEntity<List<JobResponseDto>> getMyApplications(Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Authentication is required to view your applications.");
        }
        List<JobResponseDto> appliedJobs = applicationService.findJobsAppliedBy(principal.getName());
        return ResponseEntity.ok(appliedJobs);
    }

    @PutMapping("/profiles/job-seeker/{userId}")
    public ResponseEntity<JobSeekerProfileResponseDto> updateJobSeekerProfile(@PathVariable Long userId, @Valid @RequestBody JobSeekerProfileDto profileDto, Principal principal) {
        // Centralized authorization check.
        checkAuthorization(userId, principal);

        JobSeekerProfile existingProfile = jobSeekerProfileService.getProfileByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found for user with ID: " + userId));

        // The service layer now correctly handles the update logic using the DTO.
        JobSeekerProfile updatedProfile = jobSeekerProfileService.updateProfile(existingProfile.getId(), profileDto);

        JobSeekerProfileResponseDto responseDto = profileMapper.toDto(updatedProfile);
        return ResponseEntity.ok(responseDto);
    }

    // Endpoint for updating a recruiter profile.
    @PutMapping("/profiles/recruiter/{userId}")
    public ResponseEntity<RecruiterProfileResponseDto> updateRecruiterProfile(@PathVariable Long userId, @Valid @RequestBody RecruiterProfileDto profileDto, Principal principal) {
        // Centralized authorization check.
        checkAuthorization(userId, principal);

        RecruiterProfile existingProfile = recruiterProfileService.getProfileByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found for user with ID: " + userId));

        // The service layer now correctly handles the update logic using the DTO.
        RecruiterProfile updatedProfile = recruiterProfileService.updateProfile(existingProfile.getId(), profileDto);

        RecruiterProfileResponseDto responseDto = profileMapper.toDto(updatedProfile);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/profiles/job-seeker/{userId}")
    public ResponseEntity<JobSeekerProfileResponseDto> getJobSeekerProfile(@PathVariable Long userId) {
        JobSeekerProfile profile = jobSeekerProfileService.getProfileByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found for user with ID: " + userId));
        JobSeekerProfileResponseDto responseDto = profileMapper.toDto(profile);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/profiles/recruiter/{userId}")
    public ResponseEntity<RecruiterProfileResponseDto> getRecruiterProfile(@PathVariable Long userId) {
        RecruiterProfile profile = recruiterProfileService.getProfileByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found for user with ID: " + userId));
        RecruiterProfileResponseDto responseDto = profileMapper.toDto(profile);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Helper method to verify that the authenticated user is authorized to modify a resource.
     * @param targetUserId The ID of the user resource being accessed.
     * @param principal The currently authenticated principal.
     * @throws AccessDeniedException if the user is not authenticated or not authorized.
     */
    private void checkAuthorization(Long targetUserId, Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Authentication is required to perform this action.");
        }
        User authenticatedUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found with email: " + principal.getName()));

        if (!authenticatedUser.getId().equals(targetUserId)) {
            throw new AccessDeniedException("You are not authorized to modify this profile.");
        }
    }
}