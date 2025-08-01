package com.example.project.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
