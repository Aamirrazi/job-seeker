package com.seekajob.seek_a_job.specification;

import com.seekajob.seek_a_job.entity.Job;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class JobSpecification {

    /**
     * Creates a specification to find jobs where the location contains the given string (case-insensitive).
     */
    public static Specification<Job> hasLocation(String location) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    /**
     * Creates a specification to find jobs matching a specific job type.
     */
    public static Specification<Job> hasJobType(Job.JobType jobType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("jobType"), jobType);
    }

    /**
     * Creates a specification to find jobs where the job's salary range overlaps with the desired minimum salary.
     * This finds jobs where the job's maxSalary is greater than or equal to the user's desired minSalary.
     */
    public static Specification<Job> minSalary(BigDecimal minSalary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("maxSalary"), minSalary);
    }

    /**
     * Creates a specification to find jobs where the job's salary range overlaps with the desired maximum salary.
     * This finds jobs where the job's minSalary is less than or equal to the user's desired maxSalary.
     */
    public static Specification<Job> maxSalary(BigDecimal maxSalary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("minSalary"), maxSalary);
    }
}