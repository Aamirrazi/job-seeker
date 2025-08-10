package com.seekajob.seek_a_job.repository;

//import com.example.jobseeker.entity.User;
import com.seekajob.seek_a_job.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
