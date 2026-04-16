package com.gokul.careervexjobportal.service;

import com.gokul.careervexjobportal.dto.JobRequest;
import com.gokul.careervexjobportal.dto.JobResponse;
import com.gokul.careervexjobportal.exception.ResourceNotFoundException;
import com.gokul.careervexjobportal.exception.UserNotFoundException;
import com.gokul.careervexjobportal.model.Job;
import com.gokul.careervexjobportal.model.User;
import com.gokul.careervexjobportal.repository.JobPostRepository;
import com.gokul.careervexjobportal.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobPostService {
//    @Autowired
    private final UserRepository userRepository;
//    @Autowired
    private final JobPostRepository jobPostRepository;
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs() {
        return jobPostRepository.findAll().stream().map(this::mapToResponse).toList();
    }
    @Transactional(readOnly = true)
    public JobResponse getJobById(Long id) {

        Job job =  jobPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + id));
        return mapToResponse(job);
    }

    @Transactional
    public JobResponse postJob(@Valid JobRequest request,String recruiterEmail) {

        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new UserNotFoundException("Recruiter not found"));
        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .salary(request.getSalary())
                .company(request.getCompany())
                .postedBy(recruiter)
                .skills(request.getSkills())
                .build();

        Job savedJob = jobPostRepository.save(job);
        log.info("Job posted: '{}' by recruiter {}", savedJob.getTitle(), recruiterEmail);
        return mapToResponse(savedJob);
    }
    @Transactional
    public JobResponse updateJob(Long id, @Valid JobRequest request,String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new UserNotFoundException("Recruiter not found"));
        Job job = jobPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found : "+ id));
        if (!job.getPostedBy().getEmail().equals(recruiterEmail)) {
            throw new AccessDeniedException("Unauthorized");
        }
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setCompany(request.getCompany());
        job.setPostedBy(recruiter);
        job.setSkills(request.getSkills());

        Job updatedjob = jobPostRepository.save(job);
        return mapToResponse(updatedjob);
    }

    @Transactional
    public void deleteJob(Long jobId, String userEmail, String userRole) {
        Job job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + jobId));

        boolean isAdmin = "ADMIN".equalsIgnoreCase(userRole);
        boolean isOwner = job.getPostedBy().getEmail().equals(userEmail);

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You can only delete your own job listings");
        }

        job.setIsActive(false); // Soft delete
        jobPostRepository.save(job);
        log.info("Job {} soft-deleted by {}", jobId, userEmail);
    }
    @Transactional(readOnly = true)
    public List<JobResponse> searchJobs(String keyword) {
        return jobPostRepository.searchJobs(keyword).stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getJobsByRecruiter(String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new UserNotFoundException("Recruiter not found"));
        return jobPostRepository.findByPostedBy(recruiter).stream().map(this::mapToResponse).toList();
    }

    private JobResponse mapToResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .skills(job.getSkills())
                .location(job.getLocation())
                .salary(job.getSalary())
                .company(job.getCompany())
                .postedBy(job.getPostedBy())
                .isActive(job.getIsActive())
                .createdAt(job.getCreatedAt())
                .build();
    
    }
}
