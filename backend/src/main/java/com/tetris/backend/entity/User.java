package com.tetris.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
// 對應 users 資料表，代表一個註冊使用者。
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(nullable = false, unique = true, length = 150)
	private String email;

	@Column(name = "password_hash", nullable = false, length = 64)
	private String passwordHash;

	protected User() { }

	// 建立使用者時保存已經雜湊過的密碼。
	public User(String username, String email, String passwordHash) {
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
	}

	// 重設密碼時更新密碼雜湊值。
	public void updatePasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

	public Long getId() { return id; }
	public String getUsername() { return username; }
	public String getEmail() { return email; }
	public String getPasswordHash() { return passwordHash; }
}
