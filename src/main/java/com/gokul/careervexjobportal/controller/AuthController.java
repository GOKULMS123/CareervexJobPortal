package com.gokul.careervexjobportal.controller;

import com.gokul.careervexjobportal.dto.ForgetPassword;
import com.gokul.careervexjobportal.dto.LoginRequest;
import com.gokul.careervexjobportal.dto.RegisterRequest;
import com.gokul.careervexjobportal.exception.UserNotFoundException;
import com.gokul.careervexjobportal.model.User;
import com.gokul.careervexjobportal.repository.UserRepository;
import com.gokul.careervexjobportal.security.JwtUtil;
import com.gokul.careervexjobportal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

//    @Autowired
    private final AuthService authService;
//    @Autowired
    private final AuthenticationManager authenticationManager;
//    @Autowired
    private final UserRepository userRepository;
//    @Autowired
    private final PasswordEncoder passwordEncoder;
//    @Autowired
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req){
        try{
            User user = authService.registerUser(req);
            return ResponseEntity.ok(Map.of("message", "User registered Successfully."));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {
        try {
            // This line triggers the actual password check
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getContact(), req.getPassword())
            );

            // If we reach here, authentication was successful
            User user = userRepository.findByEmailOrPhone(req.getContact(), req.getContact())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            String token = jwtUtil.generateToken(user);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.getRole(),
                    "name", user.getName(),
                    "userId", user.getId()
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email/phone or password"));
        }
    }
    @PutMapping("/forget_password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid ForgetPassword password){
        authService.updatePassword(password);
        return ResponseEntity.ok("Password Has been Updated Successfully...");
    }
}
