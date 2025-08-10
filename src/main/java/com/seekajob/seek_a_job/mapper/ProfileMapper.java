package com.seekajob.seek_a_job.mapper;

import com.seekajob.seek_a_job.dto.JobSeekerProfileResponseDto;
import com.seekajob.seek_a_job.dto.RecruiterProfileResponseDto;
import com.seekajob.seek_a_job.entity.JobSeekerProfile;
import com.seekajob.seek_a_job.entity.RecruiterProfile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public JobSeekerProfileResponseDto toDto(JobSeekerProfile profile) {
        if (profile == null) return null;
        JobSeekerProfileResponseDto dto = new JobSeekerProfileResponseDto();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setBio(profile.getBio());
        dto.setResumeUrl(profile.getResumeUrl());
        dto.setSkills(profile.getSkills());
        if (profile.getUser() != null) {
            dto.setUserId(profile.getUser().getId());
            dto.setUserEmail(profile.getUser().getEmail());
        }
        return dto;
    }

    public RecruiterProfileResponseDto toDto(RecruiterProfile profile) {
        if (profile == null) return null;
        RecruiterProfileResponseDto dto = new RecruiterProfileResponseDto();
        dto.setId(profile.getId());
        dto.setCompanyName(profile.getCompanyName());
        dto.setCompanyWebsite(profile.getCompanyWebsite());
        dto.setCompanyDescription(profile.getCompanyDescription());
        if (profile.getUser() != null) {
            dto.setUserId(profile.getUser().getId());
            dto.setUserEmail(profile.getUser().getEmail());
        }
        return dto;
    }
}