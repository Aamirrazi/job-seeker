package com.seekajob.seek_a_job.repository;

import com.seekajob.seek_a_job.entity.Application;
import com.seekajob.seek_a_job.entity.Job;
import com.seekajob.seek_a_job.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobSeekerProfile_User_Email(String email);
    boolean existsByJobAndJobSeekerProfile(Job job, JobSeekerProfile jobSeekerProfile);
}