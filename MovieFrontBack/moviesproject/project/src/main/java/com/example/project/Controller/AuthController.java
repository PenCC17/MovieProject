package com.example.project.Controller;

import com.example.project.Config.JwtUtil;
import com.example.project.Entity.Account;
import com.example.project.Service.AccountService;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
 

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AccountService accountService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            String jwt = jwtUtil.generateToken(loginRequest.getUsername()); // Generate JWT upon successful authentication
            Account account = accountService.findByUsername(loginRequest.getUsername()); // Fetch account details

            return ResponseEntity.ok(new LoginResponse(jwt, account.getUsername(), account.getAccountId())); // Return JWT and user details
        } catch (BadCredentialsException e) { // Handle authentication failure
            return ResponseEntity.status(401).body(new ErrorResponse("Unsuccessful Login"));
        }
    }


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public static class LoginRequest {
    private String username;
    private String password;
}

@Getter @Setter @AllArgsConstructor
public static class LoginResponse {
    private String token;
    private String username;
    private Long accountId;
}

@Getter @AllArgsConstructor
public static class ErrorResponse {
    private final String message;
}   

}