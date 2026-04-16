package com.gokul.careervexjobportal.controller;

import com.gokul.careervexjobportal.dto.JobRequest;
import com.gokul.careervexjobportal.dto.JobResponse;
import com.gokul.careervexjobportal.model.Job;
import com.gokul.careervexjobportal.service.JobPostService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobPostController {

//    @Autowired
    private final JobPostService jobPostService;

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs(){
        return ResponseEntity.ok(jobPostService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id){
        return ResponseEntity.ok(jobPostService.getJobById(id));
    }

    @PostMapping
    public ResponseEntity<JobResponse> postJob(@RequestBody @Valid JobRequest jobReq, @AuthenticationPrincipal UserDetails userDetails){
        JobResponse job = jobPostService.postJob(jobReq,userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request,
            @AuthenticationPrincipal UserDetails userDetails)
             {
                 JobResponse job = jobPostService.updateJob(id, request, userDetails.getUsername());
                return ResponseEntity.ok(job);
     }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .orElse("RECRUITER");
            jobPostService.deleteJob(id, userDetails.getUsername(), role);
            return ResponseEntity.ok(Map.of("message", "Job deleted successfully"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobResponse>> searchJobs(
            @RequestParam String keyword){
        return ResponseEntity.ok(jobPostService.searchJobs(keyword));
    }

    @GetMapping("/my")
    public ResponseEntity<List<JobResponse>> getMyJobs(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jobPostService.getJobsByRecruiter(userDetails.getUsername()));
    }

}
