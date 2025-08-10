package com.seekajob.seek_a_job.mapper;

import com.seekajob.seek_a_job.dto.JobResponseDto;
import com.seekajob.seek_a_job.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public JobResponseDto toDto(Job job) {
        if (job == null) {
            return null;
        }
        JobResponseDto dto = new JobResponseDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalaryDescription());
        dto.setJobType(job.getJobType());
        dto.setPostedDate(job.getPostedDate());
        if (job.getRecruiterProfile() != null) {
            dto.setRecruiterProfileId(job.getRecruiterProfile().getId());
            dto.setCompanyName(job.getRecruiterProfile().getCompanyName());
        }
        return dto;
    }
}