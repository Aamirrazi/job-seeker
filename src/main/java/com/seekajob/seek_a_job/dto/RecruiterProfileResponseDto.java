package com.seekajob.seek_a_job.dto;

import lombok.Data;

@Data
public class RecruiterProfileResponseDto {
    private Long id;
    private String companyName;
    private String companyWebsite;
    private String companyDescription;
    private Long userId;
    private String userEmail;
}