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
        Specification<Job> spec = Specification.where(null);

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

    // ... other methods (createJob, updateJob, deleteJob, findJobById) would go here ...
    // For brevity, they are omitted but would be required for a fully functional service.
    public JobResponseDto createJob(JobDto jobDto, String recruiterEmail) { /* ... */ return null; }
    public JobResponseDto updateJob(Long jobId, JobDto jobDetails, String recruiterEmail) { /* ... */ return null; }
    public Optional<JobResponseDto> findJobById(Long jobId) { /* ... */ return Optional.empty(); }
    public void deleteJob(Long jobId, String recruiterEmail) { /* ... */ }

}