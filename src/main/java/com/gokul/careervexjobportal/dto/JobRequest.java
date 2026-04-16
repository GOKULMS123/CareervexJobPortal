package com.gokul.careervexjobportal.dto;

import com.gokul.careervexjobportal.model.enums.Skills;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private BigDecimal salary;

    @Size(max = 150)
    private String company;

    @Size(max = 150)
    private String postedBy;

    private Set<Skills> skills;
}
