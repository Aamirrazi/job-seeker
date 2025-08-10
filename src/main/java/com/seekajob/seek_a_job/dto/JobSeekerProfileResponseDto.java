package com.seekajob.seek_a_job.dto;

import lombok.Data;

import java.util.Set;

@Data
public class JobSeekerProfileResponseDto {
    private Long id;
    private String name;
    private String bio;
    private String resumeUrl;
    private Set<String> skills;
    private Long userId;
    private String userEmail;
}