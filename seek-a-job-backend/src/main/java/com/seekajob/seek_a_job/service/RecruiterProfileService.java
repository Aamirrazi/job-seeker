package com.seekajob.seek_a_job.service;

import com.seekajob.seek_a_job.dto.RecruiterProfileDto;
import com.seekajob.seek_a_job.entity.RecruiterProfile;
import com.seekajob.seek_a_job.entity.User;
import com.seekajob.seek_a_job.exception.ResourceNotFoundException;
import com.seekajob.seek_a_job.repository.RecruiterProfileRepository;
import com.seekajob.seek_a_job.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UserRepository userRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public RecruiterProfile createProfile(Long userId, RecruiterProfile profile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found."));

        // Check if a recruiter profile already exists for this user
        recruiterProfileRepository.findByUserId(userId).ifPresent(p -> {
            throw new IllegalStateException("User with ID " + userId + " already has a recruiter profile.");
        });

        profile.setUser(user);
        return recruiterProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public Optional<RecruiterProfile> getProfileByUserId(Long userId) {
        return recruiterProfileRepository.findByUserId(userId);
    }

    @Transactional
    public RecruiterProfile updateProfile(Long profileId, RecruiterProfileDto profileDto) {
        RecruiterProfile existingProfile = recruiterProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile with ID " + profileId + " not found."));

        existingProfile.setCompanyName(profileDto.getCompanyName());
        existingProfile.setCompanyWebsite(profileDto.getCompanyWebsite());
        existingProfile.setCompanyDescription(profileDto.getCompanyDescription());
        return recruiterProfileRepository.save(existingProfile);
    }

    @Transactional
    public void deleteProfile(Long profileId) {
        if (!recruiterProfileRepository.existsById(profileId)) {
            throw new ResourceNotFoundException("Recruiter profile with ID " + profileId + " not found.");
        }
        recruiterProfileRepository.deleteById(profileId);
    }
}
