package com.example.project.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name="account")
@Getter @Setter @NoArgsConstructor
public class Account { 

    @NotBlank
    @Size(min=3, max=20)
    String username;

    @NotBlank
    String password;
    
    @Column(name="accountId")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long accountId;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

   
}