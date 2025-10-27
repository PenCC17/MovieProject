package com.example.project.Service;

import com.example.project.Repository.AccountRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.project.Entity.Account;

import java.util.ArrayList;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService { //Implement UserDetailsService for Spring Security
    private final AccountRepository accountRepository;
    
    public CustomUserDetailsService(AccountRepository accountRepository) { //Constructor injection of AccountRepository
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //Load user details by username
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return User.builder() //Build UserDetails object for Spring Security
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(new ArrayList<>()) //Assign empty authorities for simplicity
                .build();
}
}