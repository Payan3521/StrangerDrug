package com.desarrollox.backend.api_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend.api_auth.model.LoginAttempt;

@Repository
public interface ILoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    
}
