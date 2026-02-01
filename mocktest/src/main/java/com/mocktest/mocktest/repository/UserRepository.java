package com.mocktest.mocktest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mocktest.mocktest.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
