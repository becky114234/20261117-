package com.tetris.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tetris.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	// Spring Data JPA 會依方法名稱自動產生查詢。
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}
