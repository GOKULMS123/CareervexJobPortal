package com.gokul.careervexjobportal.dto;

import com.gokul.careervexjobportal.model.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String seekerName;
    private String seekerEmail;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private ProfileResponse profile;
}
