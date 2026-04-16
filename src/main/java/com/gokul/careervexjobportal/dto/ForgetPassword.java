package com.gokul.careervexjobportal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPassword {
    @NotBlank(message = "User (Email or Phone Number) is required")
    private String contact;
    @NotBlank(message = "Enter old password is required")
    private String oldPassword;
    @NotBlank(message = "Enter new password is required")
    private String newPassword;

}
