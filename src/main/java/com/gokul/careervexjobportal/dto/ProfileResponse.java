package com.gokul.careervexjobportal.dto;

import com.gokul.careervexjobportal.model.enums.Skills;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private Long id;
    private String userName;
    private String email;
    private String address;
    private String currentCompany;
    private String currentCtc;
    private String exceptedCtc;
    private String bio;
    private String profilePictureUrl;
    
    private List<ExperienceResponse> experiences;
    private List<EducationResponse> educations;
    
    private Set<Skills> skills;
    private String resumeUrl;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    
    private Integer completionPercentage;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
