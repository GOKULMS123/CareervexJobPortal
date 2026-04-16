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
public class EducationRequest {
    private Long id;
    @NotBlank(message = "Institution name is required")
    private String institution;
    @NotBlank(message = "Degree is required")
    private String degree;
    @NotBlank(message = "Field of Study name is required")
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    @NotBlank(message = "Percentage is required")
    private String percentage;
}
