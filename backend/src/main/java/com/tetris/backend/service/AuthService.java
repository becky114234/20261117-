package com.tetris.backend.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tetris.backend.dto.AuthDtos.AuthResponse;
import com.tetris.backend.dto.AuthDtos.LoginRequest;
import com.tetris.backend.dto.AuthDtos.RegisterRequest;
import com.tetris.backend.dto.AuthDtos.ResetPasswordRequest;
import com.tetris.backend.dto.AuthDtos.UserResponse;
import com.tetris.backend.entity.User;
import com.tetris.backend.repository.UserRepository;

@Service
// 負責帳戶驗證、密碼處理，以及登入 Token 的管理。
public class AuthService {
	private final UserRepository users;
	private final Map<String, Long> sessions = new ConcurrentHashMap<>();

	public AuthService(UserRepository users) { this.users = users; }

	// 註冊成功後直接建立登入工作階段。
	public AuthResponse register(RegisterRequest request) {
		validate(request.username(), request.email(), request.password());
		if (users.existsByUsername(request.username())) throw conflict("Username already exists");
		if (users.existsByEmail(request.email())) throw conflict("Email already exists");
		User user = users.save(new User(request.username().trim(), request.email().trim().toLowerCase(), hash(request.password())));
		return loginResponse(user);
	}

	// 查詢帳號並比對加密後的密碼。
	public AuthResponse login(LoginRequest request) {
		User user = users.findByUsername(request.username()).orElseThrow(() -> unauthorized("Invalid credentials"));
		if (!user.getPasswordHash().equals(hash(request.password()))) throw unauthorized("Invalid credentials");
		return loginResponse(user);
	}

	// 以帳號和 Email 確認身分，再更新密碼雜湊值。
	public void resetPassword(ResetPasswordRequest request) {
		validate(request.username(), request.email(), request.newPassword());
		User user = users.findByUsername(request.username().trim())
				.filter(item -> item.getEmail().equalsIgnoreCase(request.email().trim()))
				.orElseThrow(() -> unauthorized("Invalid account details"));
		user.updatePasswordHash(hash(request.newPassword()));
		users.save(user);
	}

	// 從 Bearer Token 找出使用者；找不到時回傳 null。
	public User requireUser(String authorization) {
		if (authorization == null || !authorization.startsWith("Bearer ")) throw unauthorized("Login required");
		Long userId = sessions.get(authorization.substring(7));
		return userId == null ? null : users.findById(userId).orElse(null);
	}

	// 登出就是把 Token 從記憶體中的工作階段移除。
	public void logout(String authorization) {
		if (authorization != null && authorization.startsWith("Bearer ")) sessions.remove(authorization.substring(7));
	}

	// 將 Entity 轉成不包含密碼的回應資料。
	public UserResponse response(User user) { return new UserResponse(user.getId(), user.getUsername(), user.getEmail()); }

	// 建立隨機 Token，並記錄 Token 對應的使用者 ID。
	private AuthResponse loginResponse(User user) {
		String token = UUID.randomUUID().toString();
		sessions.put(token, user.getId());
		return new AuthResponse(token, response(user));
	}

	// 檢查註冊或重設密碼所需欄位是否符合基本格式。
	private void validate(String username, String email, String password) {
		if (username == null || username.trim().length() < 3 || email == null || !email.contains("@") || password == null || password.length() < 6) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username, email and password are invalid");
		}
	}

	// 使用 SHA-256 將密碼轉成固定長度的雜湊字串，不直接保存明文密碼。
	private String hash(String value) {
		try {
			byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
			StringBuilder result = new StringBuilder();
			for (byte item : digest) result.append(String.format("%02x", item));
			return result.toString();
		} catch (NoSuchAlgorithmException exception) { throw new IllegalStateException(exception); }
	}

	private ResponseStatusException conflict(String message) { return new ResponseStatusException(HttpStatus.CONFLICT, message); }
	private ResponseStatusException unauthorized(String message) { return new ResponseStatusException(HttpStatus.UNAUTHORIZED, message); }
}
