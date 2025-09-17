package com.seekajob.seek_a_job.service;

import com.seekajob.seek_a_job.dto.JobSeekerProfileDto;
import com.seekajob.seek_a_job.entity.JobSeekerProfile;
import com.seekajob.seek_a_job.entity.User;
import com.seekajob.seek_a_job.exception.ResourceNotFoundException;
import com.seekajob.seek_a_job.repository.JobSeekerProfileRepository;
import com.seekajob.seek_a_job.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class JobSeekerProfileService {
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UserRepository userRepository;

    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UserRepository userRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public JobSeekerProfile createProfile(Long userId, JobSeekerProfile profile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found."));

        jobSeekerProfileRepository.findByUserId(userId).ifPresent(p -> {
            throw new IllegalStateException("User with ID " + userId + " already has a job seeker profile.");
        });

        profile.setUser(user);
        return jobSeekerProfileRepository.save(profile);
    }


    @Transactional(readOnly = true)
    public Optional<JobSeekerProfile> getProfileByUserId(Long userId) {
        return jobSeekerProfileRepository.findByUserId(userId);
    }

    @Transactional
    public JobSeekerProfile updateProfile(Long profileId, JobSeekerProfileDto profileDto) {
        JobSeekerProfile existingProfile = jobSeekerProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile with ID " + profileId + " not found."));

        existingProfile.setName(profileDto.getName());
        existingProfile.setBio(profileDto.getBio());
        existingProfile.setResumeUrl(profileDto.getResumeUrl());
        existingProfile.setSkills(profileDto.getSkills());
        return jobSeekerProfileRepository.save(existingProfile);
    }

    @Transactional
    public void deleteProfile(Long profileId) {
        if (!jobSeekerProfileRepository.existsById(profileId)) {
            throw new ResourceNotFoundException("Profile with ID " + profileId + " not found.");
        }
        jobSeekerProfileRepository.deleteById(profileId);
    }
}
