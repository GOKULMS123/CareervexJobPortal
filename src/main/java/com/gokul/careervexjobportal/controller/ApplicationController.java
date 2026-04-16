package com.gokul.careervexjobportal.controller;

import com.gokul.careervexjobportal.dto.ApplicationResponse;
import com.gokul.careervexjobportal.model.Application;
import com.gokul.careervexjobportal.model.enums.ApplicationStatus;
import com.gokul.careervexjobportal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
//    @Autowired
    private final ApplicationService applicationService;

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<?> applyJob(@PathVariable Long jobId, @AuthenticationPrincipal UserDetails userDetails){
        try {
            ApplicationResponse app = applicationService.applyToJob(jobId, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(app);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    // SEEKER: Get my applications
    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(applicationService.getMyApplications(userDetails.getUsername()));
    }
    // RECRUITER: Get all applicants for a job
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getApplicationsForJob(
            @PathVariable Long jobId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<ApplicationResponse> apps = applicationService
                    .getApplicationsForJob(jobId, userDetails.getUsername());
            return ResponseEntity.ok(apps);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    // RECRUITER: Update application status (triggers email)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ApplicationStatus status = ApplicationStatus.valueOf(body.get("status"));
            ApplicationResponse app = applicationService.updateStatus(id, status, userDetails.getUsername());
            return ResponseEntity.ok(app);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
