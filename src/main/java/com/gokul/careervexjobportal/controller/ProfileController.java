package com.gokul.careervexjobportal.controller;

import com.gokul.careervexjobportal.dto.ProfileRequest;
import com.gokul.careervexjobportal.dto.ProfileResponse;
import com.gokul.careervexjobportal.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

//    @Autowired
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponse response = profileService.getProfileByEmail(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody ProfileRequest req, @AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponse profileResponse = profileService.createProfile(req, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(profileResponse);
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody ProfileRequest req, @AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponse profileResponse = profileService.updateProfile(req, userDetails.getUsername());
        return ResponseEntity.ok(profileResponse);
    }

//    @PatchMapping
//    public ResponseEntity<ProfileResponse> patchProfile(@RequestBody Map<String, Object> updates, @AuthenticationPrincipal UserDetails userDetails) {
//        // Simple implementation for now: delegates to service or handles specifically
//        // For a full implementation, I'd add a patchProfile method in ProfileService
//        // But for this task, let's keep it consistent with the existing logic.
//        // I'll add the service method in a follow-up if needed, or just provide the PUT option.
//        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
//    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProfile(@AuthenticationPrincipal UserDetails userDetails) {
        profileService.deleteProfile(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
