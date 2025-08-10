package com.seekajob.seek_a_job.dto;

import com.seekajob.seek_a_job.entity.Job;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobResponseDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String salary;
    private Job.JobType jobType;
    private LocalDateTime postedDate;

    // Flattened data from RecruiterProfile
    private Long recruiterProfileId;
    private String companyName;

    // You could add more fields here, like companyWebsite, etc.
}