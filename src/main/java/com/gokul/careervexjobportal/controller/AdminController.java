package com.gokul.careervexjobportal.controller;

import com.gokul.careervexjobportal.dto.JobResponse;
import com.gokul.careervexjobportal.service.JobPostService;
import org.springframework.web.bind.annotation.RestController;
import com.gokul.careervexjobportal.model.Job;
import com.gokul.careervexjobportal.model.User;
import com.gokul.careervexjobportal.repository.ApplicationRepository;
import com.gokul.careervexjobportal.repository.JobPostRepository;
import com.gokul.careervexjobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserRepository userRepository;
    private final JobPostService jobPostService;
    private final ApplicationRepository applicationRepository;
    private final RestTemplate restTemplate;

    // GET all users
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // DELETE user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found: " + id));
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    // GET all jobs (including inactive)
    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobPostService.getAllJobs());
    }
}


