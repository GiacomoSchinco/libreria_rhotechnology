package com.rhotechnologylibri.libreria_rhotechnology.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
