package com.seekajob.seek_a_job.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seekajob.seek_a_job.entity.Application;
import com.seekajob.seek_a_job.entity.Job;
import com.seekajob.seek_a_job.entity.JobSeekerProfile;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a WHERE a.jobSeekerProfile.user.email = :email")
    List<Application> findByJobSeekerProfile_User_Email(@Param("email") String email);

    
    @Query("SELECT COUNT(a) > 0 FROM Application a WHERE a.job = :job AND a.jobSeekerProfile = :jobSeekerProfile")
    boolean existsByJobAndJobSeekerProfile(@Param("job") Job job, @Param("jobSeekerProfile") JobSeekerProfile jobSeekerProfile);

    
    @Query("SELECT a FROM Application a WHERE a.job.id = :jobId")
    List<Application> findByJobId(@Param("jobId") Long jobId);
}