package com.gokul.careervexjobportal.service;

import com.gokul.careervexjobportal.dto.ForgetPassword;
import com.gokul.careervexjobportal.dto.RegisterRequest;
import com.gokul.careervexjobportal.exception.UserNotFoundException;
import com.gokul.careervexjobportal.model.User;
import com.gokul.careervexjobportal.model.enums.Role;
import com.gokul.careervexjobportal.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
//    @Autowired
    private final UserRepository userRepository;
//    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(@Valid RegisterRequest req) {
        if (req.getEmail() != null && userRepository.existsByEmail(req.getEmail())) {
            throw new UserNotFoundException("Email already active");
        }
        if (req.getPhone() != null && userRepository.existsByPhone(req.getPhone())) {
            throw new UserNotFoundException("Phone already active");
        }
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.valueOf(req.getRole().toUpperCase()))
//                .isVerified(false)
                .createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public void updatePassword(@Valid ForgetPassword password) {
        User user = userRepository.findByEmailOrPhone(password.getContact(), password.getContact())
                .orElseThrow(() -> new RuntimeException("Email or Phone Number is Invalid, try again"));

        if (!passwordEncoder.matches(password.getOldPassword(), user.getPassword())){
            throw new RuntimeException("Old password is incorrect, try again");
        }
        user.setPassword(passwordEncoder.encode(password.getNewPassword()));
        userRepository.save(user);
    }
}
