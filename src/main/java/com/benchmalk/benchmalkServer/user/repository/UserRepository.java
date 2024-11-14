package com.benchmalk.benchmalkServer.user.repository;

import com.benchmalk.benchmalkServer.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByUserid(String userId);

    Optional<User> findByUsername(String username);
}
