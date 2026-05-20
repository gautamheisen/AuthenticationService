package com.gautam.authenticationservice.services;

import com.gautam.authenticationservice.dtos.UserDto;
import com.gautam.authenticationservice.exceptions.UserAlreadyExistsException;
import com.gautam.authenticationservice.exceptions.UserDoesNotExistException;
import com.gautam.authenticationservice.models.Session;
import com.gautam.authenticationservice.models.SessionStatus;
import com.gautam.authenticationservice.models.User;
import com.gautam.authenticationservice.repositories.SessionRepository;
import com.gautam.authenticationservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private PasswordEncoder bCryptPasswordEncoder;


    public AuthService(UserRepository userRepository, SessionRepository sessionRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = passwordEncoder;;

    }
    public UserDto signUp(String email, String password) throws UserAlreadyExistsException {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isEmpty()) {
            throw new UserAlreadyExistsException("User with " + email + " already exists.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }
    public ResponseEntity<UserDto> login(String email, String password) throws UserDoesNotExistException {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserDoesNotExistException("User with email: " + email + " doesn't exist.");
        }

        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        RandomStringUtils randomStringUtils = new RandomStringUtils();
        // TODO: Update here to use Jwt
        // Payload:
        // {
        //    userId:
        //    email:
        //    roles: [
        //    ]
        // }
        // Map<String, Object> claimsMap;
        // claimsMap.add(userId, 123);


        String token = RandomStringUtils.randomAscii(20);
        MultiValueMapAdapter<String, String > headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add("AUTH_TOKEN", token);

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);


        UserDto userDto = UserDto.from(user);
        ResponseEntity<UserDto> response = new ResponseEntity<>(
                userDto,
                headers,
                HttpStatus.OK
        );

        return response;
    }
    public SessionStatus validateToken(String Token,Long userId){
        Optional<Session> optionalSession = this.sessionRepository.findSessionByTokenAndUser_Id(Token,userId);
         if (optionalSession.isEmpty())
             return SessionStatus.INVALID;
          Session session = optionalSession.get();
          if(!session.getSessionStatus().equals(SessionStatus.ACTIVE)){
              return SessionStatus.EXPIRED;
          }
        return SessionStatus.ACTIVE;

    }
    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findSessionByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();

        session.setSessionStatus(SessionStatus.LOGGED_OUT);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }
}
