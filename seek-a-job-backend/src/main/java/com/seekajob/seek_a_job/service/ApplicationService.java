package com.seekajob.seek_a_job.service;

import com.seekajob.seek_a_job.dto.JobResponseDto;
import com.seekajob.seek_a_job.dto.JobSeekerProfileResponseDto;
import com.seekajob.seek_a_job.entity.Application;
import com.seekajob.seek_a_job.entity.Job;
import com.seekajob.seek_a_job.entity.JobSeekerProfile;
import com.seekajob.seek_a_job.entity.User;
import com.seekajob.seek_a_job.exception.ResourceNotFoundException;
import com.seekajob.seek_a_job.mapper.JobMapper;
import com.seekajob.seek_a_job.mapper.ProfileMapper;
import com.seekajob.seek_a_job.repository.ApplicationRepository;
import com.seekajob.seek_a_job.repository.JobRepository;
import com.seekajob.seek_a_job.repository.JobSeekerProfileRepository;
import com.seekajob.seek_a_job.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UserRepository userRepository;
    private final JobMapper jobMapper;

    @Transactional
    public Application applyForJob(Long jobId, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found with email: " + userEmail));

        if (user.getRole() != User.Role.JOB_SEEKER) {
            throw new AccessDeniedException("Only job seekers are allowed to apply for jobs.");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + jobId));

        // Now that we have the user, we can find their profile directly by the user's ID.
        JobSeekerProfile jobSeekerProfile = jobSeekerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Job Seeker profile not found for user: " + userEmail));

        // Prevent duplicate applications
        if (applicationRepository.existsByJobAndJobSeekerProfile(job, jobSeekerProfile)) {
            throw new IllegalStateException("You have already applied for this job.");
        }

        Application application = new Application();
        application.setJob(job);
        application.setJobSeekerProfile(jobSeekerProfile);
        application.setStatus(Application.ApplicationStatus.SUBMITTED);

        return applicationRepository.save(application);
    }

    @Transactional(readOnly = true)
    public List<JobResponseDto> findJobsAppliedBy(String userEmail) {
        List<Application> applications = applicationRepository.findByJobSeekerProfile_User_Email(userEmail);
        return applications.stream()
                .map(Application::getJob)
                .map(jobMapper::toDto)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<JobSeekerProfileResponseDto> findApplicantsForJob(Long jobId, String recruiterEmail) {
        // 1. Validate that the job exists
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + jobId));

        User recruiterUser = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter user not found with email: " + recruiterEmail));

        if (recruiterUser.getRole() != User.Role.RECRUITER) {
            throw new AccessDeniedException("Only recruiters can view applicants.");
        }

        if (!job.getRecruiterProfile().getUser().getId().equals(recruiterUser.getId())) {
            throw new AccessDeniedException("You are not authorized to view applicants for this job posting.");
        }

        // 3. Find applications for the job and map them to DTOs
        List<Application> applications = applicationRepository.findByJobId(jobId);
        return applications.stream()
                .map(Application::getJobSeekerProfile)
                .map(ProfileMapper::toDto)
                .collect(Collectors.toList());
    }
}