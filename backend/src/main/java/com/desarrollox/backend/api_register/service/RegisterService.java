package com.desarrollox.backend.api_register.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.desarrollox.backend.api_register.exception.InvalidAgeException;
import com.desarrollox.backend.api_register.exception.UserAlreadyRegisteredException;
import com.desarrollox.backend.api_register.exception.UserNotFoundException;
import com.desarrollox.backend.api_register.model.User;
import com.desarrollox.backend.api_register.model.User.Role;
import com.desarrollox.backend.api_register.repository.IRegisterRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterService implements IRegisterService {
    private final IRegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        if(registerRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserAlreadyRegisteredException("El usuario con email: " + user.getEmail() + " Ya está registrado");
        }
        if (user.getBirthdate().isAfter(LocalDate.now().minusYears(18))) {
            throw new InvalidAgeException("El usuario no es mayor de edad");
        }

        user.setRole(Role.CLIENTE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User respuesta = registerRepository.save(user);
        return respuesta;
    }

    @Override
    public Optional<User> findById(Long id) {
        if(registerRepository.existsById(id)){
            return Optional.of(registerRepository.findById(id).get());
        }else{
            throw new UserNotFoundException("El usuario con id: " + id + " No fue encontrado");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if(registerRepository.findByEmail(email).isPresent()){
            return Optional.of(registerRepository.findByEmail(email).get());
        }else{
            throw new UserNotFoundException("El usuario con email: " + email + " No fue encontrado");
        }
    }
}