package com.seekajob.seek_a_job.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seekajob.seek_a_job.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    
    Optional<User> findById(Long id);
}