package com.gokul.careervexjobportal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienceRequest {
    private Long id;
    @NotBlank(message = "Company name is required")
    private String company;
    @NotBlank(message = "Role is required")
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
