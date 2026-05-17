package com.gautam.authenticationservice.repositories;

import com.gautam.authenticationservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Session save(Session session);

    Optional<Session> findSessionsByToken(String token);
    Optional<Session> findSessionByTokenAndUser_Id(String token,Long userId);
}
