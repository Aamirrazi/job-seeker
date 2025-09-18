package com.seekajob.seek_a_job.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seekajob.seek_a_job.entity.RecruiterProfile;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Long> {
    
    @Query("SELECT rp FROM RecruiterProfile rp WHERE rp.user.id = :userId")
    Optional<RecruiterProfile> findByUserId(@Param("userId") Long userId);
}