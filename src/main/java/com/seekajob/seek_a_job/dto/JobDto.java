package com.seekajob.seek_a_job.dto;

import com.seekajob.seek_a_job.entity.Job;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating or updating a Job posting.
 * It captures all the necessary information from the client.
 */
@Data
public class JobDto {

    @NotBlank(message = "Job title cannot be blank")
    @Size(max = 255, message = "Job title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Job description cannot be blank")
    private String description;

    @NotBlank(message = "Location cannot be blank")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @Size(max = 100, message = "Salary information must not exceed 100 characters")
    private String salary; // Optional field

    @NotNull(message = "Job type must be specified (e.g., FULL_TIME, PART_TIME)")
    private Job.JobType jobType;
}
