package com.gautam.authenticationservice.services;

import com.gautam.authenticationservice.models.Session;
import com.gautam.authenticationservice.models.User;
import com.gautam.authenticationservice.repositories.SessionRepository;
import com.gautam.authenticationservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;

    }
    public void signUp(User user){
       this.userRepository.save(user);
    }
    public String login(User user){
        User savedUser = this.userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!savedUser.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        Session session = new Session();
        String token = "xyz123abc";
        session.setToken(token);
        session.setUser(savedUser);
        this.sessionRepository.save(session);
        return token;
    }

    public boolean validateToken(String Token){
        Optional<Session> session = this.sessionRepository.findSessionsByToken(Token);
         if (session.isEmpty())
             return false;

           return true;
    }
}
