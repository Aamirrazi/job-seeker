package com.seekajob.seek_a_job.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seekajob.seek_a_job.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    
    @Query("SELECT j FROM Job j WHERE j.recruiterProfile.user.email = :recruiterEmail")
    Page<Job> findByRecruiterProfile_User_Email(@Param("recruiterEmail") String recruiterEmail, Pageable pageable);
}