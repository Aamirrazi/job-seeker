package com.seekajob.seek_a_job.controller;

import com.seekajob.seek_a_job.dto.JobDto;
import com.seekajob.seek_a_job.dto.JobResponseDto;
import com.seekajob.seek_a_job.entity.Application;
import com.seekajob.seek_a_job.entity.Job;
import com.seekajob.seek_a_job.exception.ResourceNotFoundException;
import com.seekajob.seek_a_job.service.ApplicationService;
import com.seekajob.seek_a_job.service.JobService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    private final ApplicationService applicationService;

    public JobController(JobService jobService, ApplicationService applicationService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    // Recruiter endpoint: Create a new job
    // The recruiter's identity should come from the security context (Principal).
    @PostMapping
    public ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody JobDto jobDto, Principal principal) {
        // The service would use principal.getName() (which is the user's email/username) to find the user.
        // principal.getName() returns the username, which we've set as the email.
        JobResponseDto newJobDto = jobService.createJob(jobDto, principal.getName());
        return new ResponseEntity<>(newJobDto, HttpStatus.CREATED);
    }

    // Recruiter endpoint: Update an existing job
    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponseDto> updateJob(@PathVariable Long jobId, @Valid @RequestBody JobDto jobDetails, Principal principal) {
        // In a real app, you should also verify that the authenticated user owns this job
        // The service layer now handles the authorization check.
        JobResponseDto updatedJobDto = jobService.updateJob(jobId, jobDetails, principal.getName());
        return ResponseEntity.ok(updatedJobDto);
    }

    // Public endpoint: Search for all jobs
    @GetMapping
    public ResponseEntity<Page<JobResponseDto>> getAllJobs(
            Pageable pageable,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Job.JobType jobType,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary
    ) {
        Page<JobResponseDto> jobsPage = jobService.findAllJobs(pageable, location, jobType, minSalary, maxSalary);
        return ResponseEntity.ok(jobsPage);
    }

    // Public endpoint: Get a single job by ID
    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponseDto> getJobById(@PathVariable Long jobId) {
        JobResponseDto jobDto = jobService.findJobById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + jobId));
        return ResponseEntity.ok(jobDto);
    }

    // Recruiter endpoint: Delete a job
    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId, Principal principal) {
        // In a real app, you should also verify that the authenticated user owns this job
        jobService.deleteJob(jobId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    // Job seeker endpoint: Apply for a job
    // The job seeker's identity should come from the security context (Principal).
    @PostMapping("/{jobId}/apply")
    public ResponseEntity<?> applyForJob(@PathVariable Long jobId, Principal principal) {
        Application application = applicationService.applyForJob(jobId, principal.getName());
        return new ResponseEntity<>(Map.of("message", "Application submitted successfully", "applicationId", application.getId()), HttpStatus.CREATED);
    }
}