package com.anna.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anna.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
}
