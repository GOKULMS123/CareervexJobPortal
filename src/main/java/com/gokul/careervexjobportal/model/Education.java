package com.gokul.careervexjobportal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "educations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String institution;
    @Column(nullable = false, length = 255)
    private String degree;
    @Column(nullable = false, length = 255)
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    @Column(nullable = false, length = 10)
    private String percentage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
