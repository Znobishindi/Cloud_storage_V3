package com.example.cloud_storage_v3.repository;

import com.example.cloud_storage_v3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    boolean existsUserByLogin(String login);
}
