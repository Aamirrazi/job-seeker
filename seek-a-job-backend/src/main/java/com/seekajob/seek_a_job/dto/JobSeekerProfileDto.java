package com.seekajob.seek_a_job.dto;

import lombok.Data;

import java.util.Set;

@Data
public class JobSeekerProfileDto {
    private String name;
    private String bio;
    private String resumeUrl;
    private Set<String> skills;
}