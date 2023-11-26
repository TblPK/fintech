package com.weather.controllers;

import com.weather.models.User;
import com.weather.services.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final MyUserDetailsService myUserDetailsService;

    @PostMapping("/reg/reg")
    @PreAuthorize("isAnonymous()")
    public String registration(@RequestBody User user) {
        myUserDetailsService.registration(user);

        return "successful";
    }

}
