package com.gokul.careervexjobportal.dto;

import com.gokul.careervexjobportal.model.enums.Skills;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRequest {
    @NotBlank(message = "Address is required")
    private String address;

    private String currentCompany;
    private String currentCtc;
    private String exceptedCtc;

    @Size(max = 1500, message = "Bio cannot exceed 1500 characters")
    private String bio;

    private String profilePictureUrl;

    private List<ExperienceRequest> experiences;
    private List<EducationRequest> educations;

    private Set<Skills> skills;
    
    @URL(message = "Invalid resume URL")
    private String resumeUrl;
    
    @URL(message = "Invalid LinkedIn URL")
    private String linkedinUrl;
    
    @URL(message = "Invalid GitHub URL")
    private String githubUrl;
    
    @URL(message = "Invalid Portfolio URL")
    private String portfolioUrl;
}
