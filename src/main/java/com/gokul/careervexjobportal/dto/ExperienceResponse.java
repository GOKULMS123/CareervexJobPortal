package com.gokul.careervexjobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienceResponse {
    private Long id;
    private String company;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
