package com.gautam.authenticationservice.controllers;

import com.gautam.authenticationservice.dtos.*;
import com.gautam.authenticationservice.exceptions.UserAlreadyExistsException;
import com.gautam.authenticationservice.exceptions.UserDoesNotExistException;
import com.gautam.authenticationservice.models.SessionStatus;
import com.gautam.authenticationservice.models.User;
import com.gautam.authenticationservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
   private AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validate(@RequestBody ValidateTokenRequestDto validateRequest){
        SessionStatus status =  this.authService.validateToken(validateRequest.getToken(),validateRequest.getUserId());
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) throws UserDoesNotExistException {
        return authService.login(request.getEmail(), request.getPassword());
    }



    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) throws UserAlreadyExistsException {
        UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request) {
        return authService.logout(request.getToken(), request.getUserId());
    }


}
