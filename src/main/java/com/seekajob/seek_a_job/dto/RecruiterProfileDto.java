package com.seekajob.seek_a_job.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

/**
 * DTO for carrying Recruiter Profile data. Used for creating and updating profiles.
 * This ensures a clean separation between the API layer and the persistence (entity) layer.
 */
@Data
public class RecruiterProfileDto {

    @NotBlank(message = "Company name cannot be blank")
    @Size(max = 255, message = "Company name must not exceed 255 characters")
    private String companyName;

    @URL(message = "Please provide a valid company website URL")
    @Size(max = 255, message = "Company website URL must not exceed 255 characters")
    private String companyWebsite;

    @Size(max = 2000, message = "Company description must not exceed 2000 characters")
    private String companyDescription;
}
