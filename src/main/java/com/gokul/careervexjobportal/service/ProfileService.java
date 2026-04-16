package com.gokul.careervexjobportal.service;

import com.gokul.careervexjobportal.dto.*;

import com.gokul.careervexjobportal.exception.UserNotFoundException;
import com.gokul.careervexjobportal.model.*;
import com.gokul.careervexjobportal.repository.ProfileRepository;
import com.gokul.careervexjobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {

//    @Autowired
    private final UserRepository userRepository;

//    @Autowired
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public ProfileResponse getProfileByEmail(String email) {
        Profile profile = profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Profile not found for user: " + email));
        return mapToResponse(profile);
    }

    @Transactional
    public ProfileResponse createProfile(ProfileRequest req, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (profileRepository.findByUserEmail(email).isPresent()) {
            throw new RuntimeException("Profile already exists for user: " + email);
        }

        Profile profile = Profile.builder()
                .user(user)
                .address(req.getAddress())
                .currentCompany(req.getCurrentCompany())
                .currentCtc(req.getCurrentCtc())
                .exceptedCtc(req.getExceptedCtc())
                .bio(req.getBio())
                .profilePictureUrl(req.getProfilePictureUrl())
                .resumeUrl(req.getResumeUrl())
                .linkedinUrl(req.getLinkedinUrl())
                .githubUrl(req.getGithubUrl())
                .portfolioUrl(req.getPortfolioUrl())
                .skills(req.getSkills())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (req.getExperiences() != null) {
            profile.setExperiences(req.getExperiences().stream()
                    .map(expReq -> Experience.builder()
                            .company(expReq.getCompany())
                            .role(expReq.getRole())
                            .startDate(expReq.getStartDate())
                            .endDate(expReq.getEndDate())
                            .description(expReq.getDescription())
                            .profile(profile)
                            .build())
                    .collect(Collectors.toList()));
        }

        if (req.getEducations() != null) {
            profile.setEducations(req.getEducations().stream()
                    .map(eduReq -> Education.builder()
                            .institution(eduReq.getInstitution())
                            .degree(eduReq.getDegree())
                            .fieldOfStudy(eduReq.getFieldOfStudy())
                            .startDate(eduReq.getStartDate())
                            .endDate(eduReq.getEndDate())
                            .percentage(eduReq.getPercentage())
                            .profile(profile)
                            .build())
                    .collect(Collectors.toList()));
        }

        Profile savedProfile = profileRepository.save(profile);
        log.info("Profile created for user: {}", email);
        return mapToResponse(savedProfile);
    }

    @Transactional
    public ProfileResponse updateProfile(ProfileRequest req, String email) {
        Profile profile = profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Profile not found for user: " + email));

        profile.setAddress(req.getAddress());
        profile.setCurrentCompany(req.getCurrentCompany());
        profile.setCurrentCtc(req.getCurrentCtc());
        profile.setExceptedCtc(req.getExceptedCtc());
        profile.setBio(req.getBio());
        profile.setProfilePictureUrl(req.getProfilePictureUrl());
        profile.setResumeUrl(req.getResumeUrl());
        profile.setLinkedinUrl(req.getLinkedinUrl());
        profile.setGithubUrl(req.getGithubUrl());
        profile.setPortfolioUrl(req.getPortfolioUrl());
        profile.setSkills(req.getSkills());
        profile.setUpdatedAt(LocalDateTime.now());

        // ============================
        // ✅ EXPERIENCE UPDATE LOGIC
        // ============================
        Map<Long, Experience> existingExpMap = profile.getExperiences()
                .stream()
                .collect(Collectors.toMap(Experience::getId, exp -> exp));

        List<Experience> updatedExperiences = new ArrayList<>();

        if (req.getExperiences() != null) {
            for (ExperienceRequest expReq : req.getExperiences()) {

                if (expReq.getId() != null && existingExpMap.containsKey(expReq.getId())) {
                    // 🔹 Update existing
                    Experience exp = existingExpMap.get(expReq.getId());
                    exp.setCompany(expReq.getCompany());
                    exp.setRole(expReq.getRole());
                    exp.setStartDate(expReq.getStartDate());
                    exp.setEndDate(expReq.getEndDate());
                    exp.setDescription(expReq.getDescription());

                    updatedExperiences.add(exp);

                } else {
                    // 🔹 Create new
                    Experience newExp = Experience.builder()
                            .company(expReq.getCompany())
                            .role(expReq.getRole())
                            .startDate(expReq.getStartDate())
                            .endDate(expReq.getEndDate())
                            .description(expReq.getDescription())
                            .profile(profile)
                            .build();

                    updatedExperiences.add(newExp);
                }
            }
        }

        profile.getExperiences().clear();
        profile.getExperiences().addAll(updatedExperiences);


        // ============================
        // ✅ EDUCATION UPDATE LOGIC
        // ============================
        Map<Long, Education> existingEduMap = profile.getEducations()
                .stream()
                .collect(Collectors.toMap(Education::getId, edu -> edu));

        List<Education> updatedEducations = new ArrayList<>();

        if (req.getEducations() != null) {
            for (EducationRequest eduReq : req.getEducations()) {

                if (eduReq.getId() != null && existingEduMap.containsKey(eduReq.getId())) {
                    // 🔹 Update existing
                    Education edu = existingEduMap.get(eduReq.getId());
                    edu.setInstitution(eduReq.getInstitution());
                    edu.setDegree(eduReq.getDegree());
                    edu.setFieldOfStudy(eduReq.getFieldOfStudy());
                    edu.setStartDate(eduReq.getStartDate());
                    edu.setEndDate(eduReq.getEndDate());
                    edu.setPercentage(eduReq.getPercentage());

                    updatedEducations.add(edu);

                } else {
                    // 🔹 Create new
                    Education newEdu = Education.builder()
                            .institution(eduReq.getInstitution())
                            .degree(eduReq.getDegree())
                            .fieldOfStudy(eduReq.getFieldOfStudy())
                            .startDate(eduReq.getStartDate())
                            .endDate(eduReq.getEndDate())
                            .percentage(eduReq.getPercentage())
                            .profile(profile)
                            .build();

                    updatedEducations.add(newEdu);
                }
            }
        }

        profile.getEducations().clear();
        profile.getEducations().addAll(updatedEducations);

        Profile updatedProfile = profileRepository.save(profile);
        log.info("Profile updated for user: {}", email);
        return mapToResponse(updatedProfile);
    }

    @Transactional
    public void deleteProfile(String email) {
        Profile profile = profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Profile not found for user: " + email));
        profileRepository.delete(profile);
        log.info("Profile deleted for user: {}", email);
    }

    public ProfileResponse mapToResponse(Profile profile) {
        ProfileResponse response = ProfileResponse.builder()
                .id(profile.getId())
                .userName(profile.getUser().getName())
                .email(profile.getUser().getEmail())
                .address(profile.getAddress())
                .currentCompany(profile.getCurrentCompany())
                .currentCtc(profile.getCurrentCtc())
                .exceptedCtc(profile.getExceptedCtc())
                .bio(profile.getBio())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .skills(profile.getSkills())
                .resumeUrl(profile.getResumeUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .githubUrl(profile.getGithubUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();

        if (profile.getExperiences() != null) {
            response.setExperiences(profile.getExperiences().stream()
                    .map(exp -> ExperienceResponse.builder()
                            .id(exp.getId())
                            .company(exp.getCompany())
                            .role(exp.getRole())
                            .startDate(exp.getStartDate())
                            .endDate(exp.getEndDate())
                            .description(exp.getDescription())
                            .build())
                    .collect(Collectors.toList()));
        } else {
            response.setExperiences(new ArrayList<>());
        }

        if (profile.getEducations() != null) {
            response.setEducations(profile.getEducations().stream()
                    .map(edu -> EducationResponse.builder()
                            .id(edu.getId())
                            .institution(edu.getInstitution())
                            .degree(edu.getDegree())
                            .fieldOfStudy(edu.getFieldOfStudy())
                            .startDate(edu.getStartDate())
                            .endDate(edu.getEndDate())
                            .percentage(edu.getPercentage())
                            .build())
                    .collect(Collectors.toList()));
        } else {
            response.setEducations(new ArrayList<>());
        }

        response.setCompletionPercentage(calculateCompletionPercentage(profile));

        return response;
    }

    private Integer calculateCompletionPercentage(Profile profile) {
        int totalFields = 10;
        int filledFields = 0;

        if (profile.getAddress() != null && !profile.getAddress().isBlank()) filledFields++;
        if (profile.getCurrentCompany() != null && !profile.getCurrentCompany().isBlank()) filledFields++;
        if (profile.getBio() != null && !profile.getBio().isBlank()) filledFields++;
        if (profile.getProfilePictureUrl() != null && !profile.getProfilePictureUrl().isBlank()) filledFields++;
        if (profile.getExperiences() != null && !profile.getExperiences().isEmpty()) filledFields++;
        if (profile.getEducations() != null && !profile.getEducations().isEmpty()) filledFields++;
        if (profile.getSkills() != null && !profile.getSkills().isEmpty()) filledFields++;
        if (profile.getResumeUrl() != null && !profile.getResumeUrl().isBlank()) filledFields++;
        if (profile.getLinkedinUrl() != null && !profile.getLinkedinUrl().isBlank()) filledFields++;
        if ((profile.getGithubUrl() != null && !profile.getGithubUrl().isBlank()) ||
            (profile.getPortfolioUrl() != null && !profile.getPortfolioUrl().isBlank())) filledFields++;

        return (filledFields * 100) / totalFields;
    }
}
