package com.seekajob.seek_a_job.repository;

import org.springframework.data.domain.Pageable;
import com.seekajob.seek_a_job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    Page<Job> findByRecruiterProfile_User_Email(String recruiterEmail, Pageable pageable);
}