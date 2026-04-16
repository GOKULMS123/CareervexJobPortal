package com.gokul.careervexjobportal.repository;

import com.gokul.careervexjobportal.model.Application;
import com.gokul.careervexjobportal.model.Job;
import com.gokul.careervexjobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsBySeekerAndJob(User seeker, Job job);

    List<Application> findBySeeker(User seeker);

    Optional<Application> findBySeekerAndJob(User seeker, Job job);

    List<Application> findByJob(Job job);
}
