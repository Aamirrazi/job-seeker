package com.seekajob.seek_a_job.service;

import com.seekajob.seek_a_job.dto.JobDto;
import com.seekajob.seek_a_job.dto.JobResponseDto;
import com.seekajob.seek_a_job.entity.Job;
import com.seekajob.seek_a_job.entity.RecruiterProfile;
import com.seekajob.seek_a_job.entity.User;
import com.seekajob.seek_a_job.exception.ResourceNotFoundException;
import com.seekajob.seek_a_job.mapper.JobMapper;
import com.seekajob.seek_a_job.repository.JobRepository;
import com.seekajob.seek_a_job.repository.RecruiterProfileRepository;
import com.seekajob.seek_a_job.repository.UserRepository;
import com.seekajob.seek_a_job.specification.JobSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;
    private final JobMapper jobMapper;

    @Transactional(readOnly = true)
    public Page<JobResponseDto> findAllJobs(Pageable pageable, String location, Job.JobType jobType, BigDecimal minSalary, BigDecimal maxSalary) {
        // Start with a specification that does nothing.
        Specification<Job> spec = Specification.unrestricted();

        if (location != null && !location.isBlank()) {
            spec = spec.and(JobSpecification.hasLocation(location));
        }

        if (jobType != null) {
            spec = spec.and(JobSpecification.hasJobType(jobType));
        }

        if (minSalary != null) {
            spec = spec.and(JobSpecification.minSalary(minSalary));
        }

        if (maxSalary != null) {
            spec = spec.and(JobSpecification.maxSalary(maxSalary));
        }

        Page<Job> jobPage = jobRepository.findAll(spec, pageable);
        return jobPage.map(jobMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<JobResponseDto> findJobsPostedByRecruiter(String recruiterEmail, Pageable pageable) {
        // The repository method directly fetches jobs associated with the recruiter's email.
        Page<Job> jobPage = jobRepository.findByRecruiterProfile_User_Email(recruiterEmail, pageable);

        // Map the retrieved entities to DTOs and return the paginated result.
        return jobPage.map(jobMapper::toDto);
    }

    @Transactional
    public JobResponseDto createJob(JobDto jobDto, String recruiterEmail) {
        // Find the recruiter profile based on the authenticated user's email.
        User user = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + recruiterEmail));

        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found for user: " + recruiterEmail));

        Job newJob = new Job();
        // The Job entity fields are now populated using the DTO.
        newJob.setTitle(jobDto.getTitle());
        newJob.setDescription(jobDto.getDescription());
        newJob.setLocation(jobDto.getLocation());
        newJob.setSalaryDescription(jobDto.getSalary());
        newJob.setJobType(jobDto.getJobType());
        newJob.setRecruiterProfile(recruiterProfile);

        // You could add logic here to parse the salary description into min/max salaries,
        // but for now, we'll just set them to null.
        // For example:
        // parseSalary(jobDto.getSalary(), newJob);

        Job savedJob = jobRepository.save(newJob);
        return jobMapper.toDto(savedJob);
    }

    @Transactional
    public JobResponseDto updateJob(Long jobId, JobDto jobDetails, String recruiterEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + jobId));

        // Authorization check: Ensure the recruiter updating the job is the one who posted it.
        if (!job.getRecruiterProfile().getUser().getEmail().equals(recruiterEmail)) {
            throw new AccessDeniedException("You are not authorized to update this job posting.");
        }

        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setLocation(jobDetails.getLocation());
        job.setSalaryDescription(jobDetails.getSalary());
        job.setJobType(jobDetails.getJobType());

        // Again, you could parse salary info here.
        // parseSalary(jobDetails.getSalary(), job);

        Job updatedJob = jobRepository.save(job);
        return jobMapper.toDto(updatedJob);
    }

    @Transactional
    public void deleteJob(Long jobId, String recruiterEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + jobId));

        // Authorization check: Ensure the recruiter deleting the job is the one who posted it.
        if (!job.getRecruiterProfile().getUser().getEmail().equals(recruiterEmail)) {
            throw new AccessDeniedException("You are not authorized to delete this job posting.");
        }

        jobRepository.delete(job);
    }

    @Transactional(readOnly = true)
    public Optional<JobResponseDto> findJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .map(jobMapper::toDto);
    }

    // A helper method could be implemented to parse salary strings, but is not required by the prompt.
    // private void parseSalary(String salaryDescription, Job job) { ... }
}