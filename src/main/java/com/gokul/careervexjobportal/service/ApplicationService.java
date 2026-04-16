package com.gokul.careervexjobportal.service;

import com.gokul.careervexjobportal.dto.ApplicationResponse;
import com.gokul.careervexjobportal.exception.ResourceNotFoundException;
import com.gokul.careervexjobportal.exception.UserNotFoundException;
import com.gokul.careervexjobportal.model.Application;
import com.gokul.careervexjobportal.model.Job;
import com.gokul.careervexjobportal.model.Profile;
import com.gokul.careervexjobportal.model.User;
import com.gokul.careervexjobportal.model.enums.ApplicationStatus;
import com.gokul.careervexjobportal.repository.ApplicationRepository;
import com.gokul.careervexjobportal.repository.JobPostRepository;
import com.gokul.careervexjobportal.repository.ProfileRepository;
import com.gokul.careervexjobportal.repository.UserRepository;
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
public class ApplicationService {

//    @Autowired
    private final UserRepository userRepository;
//    @Autowired
    private final JobPostRepository jobPostRepository;
//    @Autowired
    private final ApplicationRepository applicationRepository;
//    @Autowired
    private final ProfileRepository profileRepository;
//    @Autowired
    private final ProfileService profileService;

    @Transactional
    public ApplicationResponse applyToJob(Long jobId, String seekerEmail) {
        User seeker = userRepository.findByEmail(seekerEmail)
                .orElseThrow(() -> new UserNotFoundException("Seeker not found"));

        Job job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + jobId));

        if (!job.getIsActive()) {
            throw new RuntimeException("This job is no longer active");
        }

        // Check if user has a profile
        Profile profile = profileRepository.findByUserEmail(seekerEmail)
                .orElseThrow(() -> new UserNotFoundException("Please complete your profile before applying for jobs."));

        // Prevent duplicate applications
        if (applicationRepository.existsBySeekerAndJob(seeker, job)) {
            throw new RuntimeException("You have already applied to this job");
        }
        Application application = Application.builder()
                .job(job)
                .seeker(seeker)
                .profile(profile)
                .status(ApplicationStatus.APPLIED)
                .build();
        Application savedApp =  applicationRepository.save(application);
        log.info("Seeker {} applied to job {} using profile {}", seekerEmail, jobId, profile.getId());
        return mapToResponse(savedApp);
    }

    public List<ApplicationResponse> getMyApplications(String seekerEmail) {
        User seeker = userRepository.findByEmail(seekerEmail)
                .orElseThrow(() -> new UserNotFoundException("Seeker not found"));
        return applicationRepository.findBySeeker(seeker).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsForJob(Long jobId, String recruiterEmail) {
        Job job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + jobId));

        // Verify recruiter owns this job
        if (job.getPostedBy() != null &&
                !job.getPostedBy().getEmail().equals(recruiterEmail)) {
            throw new AccessDeniedException("You can only view applicants for your own jobs");
        }

        return applicationRepository.findByJob(job).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public ApplicationResponse updateStatus(Long applicationId, ApplicationStatus status, String recruiterEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + applicationId));

        // Validate recruiter owns the job
        Job job = application.getJob();
        if (job.getPostedBy() != null &&
                !job.getPostedBy().getEmail().equals(recruiterEmail)) {
            throw new AccessDeniedException("You can only update status for applications to your jobs");
        }

        application.setStatus(status);
        Application saved = applicationRepository.save(application);

        log.info("Application {} status updated to {} by recruiter {}", applicationId, status, recruiterEmail);
        return mapToResponse(saved);
    }

    private ApplicationResponse mapToResponse(Application app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .jobId(app.getJob().getId())
                .jobTitle(app.getJob().getTitle())
                .companyName(app.getJob().getCompany())
                .seekerName(app.getSeeker().getName())
                .seekerEmail(app.getSeeker().getEmail())
                .status(app.getStatus())
                .appliedAt(app.getAppliedAt())
                .profile(profileService.mapToResponse(app.getProfile()))
                .build();
    }
}
