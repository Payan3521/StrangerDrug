package com.desarrollox.backend.api_register.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.desarrollox.backend.api_register.model.User;

public interface IRegisterRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}