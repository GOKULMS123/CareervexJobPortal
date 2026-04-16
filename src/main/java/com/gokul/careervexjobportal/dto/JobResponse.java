package com.gokul.careervexjobportal.dto;

import com.gokul.careervexjobportal.model.User;
import com.gokul.careervexjobportal.model.enums.Skills;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {

    private Long id;

    private String title;

    private String description;

    private Set<Skills> skills;

    private String location;

    private BigDecimal salary;

    private String company;

    private User postedBy;

    private Boolean isActive = true;

    private LocalDateTime createdAt;
}
