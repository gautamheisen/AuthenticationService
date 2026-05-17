package com.gautam.authenticationservice.controllers;

import com.gautam.authenticationservice.dtos.SessionDto;
import com.gautam.authenticationservice.dtos.UserDto;
import com.gautam.authenticationservice.models.User;
import com.gautam.authenticationservice.services.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
   private AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/signup")
    public void signUp(@RequestBody UserDto userDto){
        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
      this.authService.signUp(user);
    }
    @PostMapping("/validate")
    public boolean validate(@RequestBody SessionDto sessionDto){
         return this.authService.validateToken(sessionDto.getToken());
    }
    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto){
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
       return this.authService.login(user);
    }


}
