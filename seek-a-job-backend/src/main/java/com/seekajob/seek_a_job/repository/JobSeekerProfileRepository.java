package com.seekajob.seek_a_job.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seekajob.seek_a_job.entity.JobSeekerProfile;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Long> {
    
    @Query("SELECT jsp FROM JobSeekerProfile jsp WHERE jsp.user.email = :email")
    Optional<JobSeekerProfile> findByUser_Email(@Param("email") String email);

  
    @Query("SELECT jsp FROM JobSeekerProfile jsp WHERE jsp.user.id = :userId")
    Optional<JobSeekerProfile> findByUserId(@Param("userId") Long userId);
}