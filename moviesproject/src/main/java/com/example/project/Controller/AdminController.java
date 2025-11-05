package com.example.project.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.project.Entity.Account;
import com.example.project.Entity.Role;
import com.example.project.Repository.AccountRepository;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final AccountRepository accountRepository;

    public AdminController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Account>> getAllUsers() {
        List<Account> users = accountRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> changeUserRole(@PathVariable Long userId, @RequestParam String role) {
        Account user = accountRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Role newRole = Role.valueOf(role.toUpperCase());
            user.setRole(newRole);
            accountRepository.save(user);
            return ResponseEntity.ok("User role updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role: " + role);
        }
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        
        Account userToDelete = accountRepository.findById(userId).orElse(null);
        if (userToDelete == null) {
            return ResponseEntity.notFound().build();
        }

        // Prevent admin from deleting themselves
        if (userToDelete.getUsername().equals(currentUsername)) {
            return ResponseEntity.badRequest().body("Cannot delete your own account");
        }

        accountRepository.delete(userToDelete);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("Welcome to Admin Dashboard");
    }
}