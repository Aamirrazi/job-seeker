package com.seekajob.seek_a_job.repository;

import com.seekajob.seek_a_job.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Long> {
    Optional<JobSeekerProfile> findByUser_Email(String email);
    Optional<JobSeekerProfile> findByUserId(Long userId);
}