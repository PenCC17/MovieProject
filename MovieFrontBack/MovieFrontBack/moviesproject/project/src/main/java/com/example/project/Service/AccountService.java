package com.example.project.Service;

import com.example.project.Entity.Account;
import com.example.project.Repository.AccountRepository;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;



@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder; //Added PasswordEncoder field


    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder; //Initialize PasswordEncoder via constructor
    }

    @Transactional
    public Account register (Account acc){
        acc.setPassword(passwordEncoder.encode(acc.getPassword())); //Encode password before saving
        return accountRepository.save(acc);
    }

    public Account Login (Account acc) {
       Account existingAccount = accountRepository.findByUsername(acc.getUsername());
         if (existingAccount != null && passwordEncoder.matches(acc.getPassword(), existingAccount.getPassword())) {
              return existingAccount;
         }
         return null;
    }

    public Account getAccountById(Long accountId){
        return accountRepository.findByAccountId(accountId);
    }

    public List<Account> findAllAccounts(){
        return accountRepository.findAll();
    }

    public Account findByUsername(String username){
        
        return accountRepository.findByUsername(username);
        //System.out.println("username of test is " + test.getUsername());
    }

}