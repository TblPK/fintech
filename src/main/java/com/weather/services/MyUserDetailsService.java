package com.weather.services;

import com.weather.exception.IncorrectUsernameOrPasswordException;
import com.weather.exception.UserAlreadyExistsException;
import com.weather.models.User;
import com.weather.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IncorrectUsernameOrPasswordException("Incorrect username or password")
        );
    }

    public void registration(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(v -> {
            throw new UserAlreadyExistsException("User already exists with username: " + user.getUsername());
        });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
