package com.desarrollox.backend.api_register.service;

import java.util.Optional;
import com.desarrollox.backend.api_register.model.User;

public interface IRegisterService {
    User create(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}