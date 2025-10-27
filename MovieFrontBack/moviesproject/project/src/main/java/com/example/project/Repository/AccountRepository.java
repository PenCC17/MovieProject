package com.example.project.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.project.Entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsernameAndPassword(String username, String password);
    Account findByUsername(String username);
    Account findByAccountId(Long accountId);

}
